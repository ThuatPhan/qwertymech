package org.example.paymentservice.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import jakarta.servlet.http.HttpServletRequest;

import org.example.event.dto.OrderEvent;
import org.example.paymentservice.config.VNPayConfig;
import org.example.paymentservice.dto.VNPayResponse;
import org.example.paymentservice.dto.response.OrderResponse;
import org.example.paymentservice.repository.httpClient.OrderClient;
import org.example.paymentservice.util.VNPayUtil;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VNPayService {
    VNPayConfig vnpayConfig;
    OrderClient orderClient;
    KafkaTemplate<String, Object> kafkaTemplate;

    public VNPayResponse createPaymentUrl(HttpServletRequest request, Long orderId) {
        OrderResponse order = orderClient.getOrder(orderId).getData();

        long amount = order.getTotalPrice().longValueExact();

        Map<String, String> vnpParams = buildParams(request, orderId, amount);

        String queryUrl = VNPayUtil.buildQuery(vnpParams, true);
        String hashData = VNPayUtil.buildQuery(vnpParams, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnpayConfig.getSecretKey(), hashData);

        String paymentUrl = vnpayConfig.getPayUrl() + "?" + queryUrl + "&vnp_SecureHash=" + vnpSecureHash;

        return VNPayResponse.builder().paymentUrl(paymentUrl).build();
    }

    public VNPayResponse handlePaymentCallback(HttpServletRequest servletRequest) {
        String responseCode = servletRequest.getParameter("vnp_ResponseCode");
        String orderId = servletRequest.getParameter("vnp_TxnRef");

        kafkaTemplate.send(
                "order-delivery",
                OrderEvent.builder()
                        .param(Map.of("orderId", orderId, "status", responseCode.equals("00") ? "success" : "failed"))
                        .build());

        return VNPayResponse.builder().code(responseCode).message(responseCode.equals("00") ? "success" : "failed").build();
    }

    private Map<String, String> buildParams(HttpServletRequest request, Long orderId, long amount) {
        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", vnpayConfig.getVersion());
        vnpParams.put("vnp_Command", vnpayConfig.getCommand());
        vnpParams.put("vnp_TmnCode", vnpayConfig.getTmnCode());
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_TxnRef", orderId.toString());
        vnpParams.put("vnp_OrderInfo", "Thanh toan don hang " + orderId);
        vnpParams.put("vnp_OrderType", vnpayConfig.getOrderType());
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_ReturnUrl", vnpayConfig.getReturnUrl());
        vnpParams.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));
        vnpParams.put("vnp_Amount", String.valueOf(amount * 100));

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

        String vnpCreateDate = formatter.format(calendar.getTime());
        vnpParams.put("vnp_CreateDate", vnpCreateDate);

        calendar.add(Calendar.MINUTE, 15);
        String vnpExpireDate = formatter.format(calendar.getTime());
        vnpParams.put("vnp_ExpireDate", vnpExpireDate);

        return vnpParams;
    }
}
