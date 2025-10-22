package org.example.paymentservice.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.example.paymentservice.dto.VNPayResponse;
import org.example.paymentservice.dto.response.ApiResponse;
import org.example.paymentservice.service.VNPayService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {
    VNPayService vnPayService;

    @PostMapping("/create-payment/{orderId}/vnpay")
    public ApiResponse<VNPayResponse> createPayment(HttpServletRequest request, @PathVariable Long orderId) {
        return ApiResponse.success(vnPayService.createPaymentUrl(request, orderId));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/vnpay/callback")
    public ApiResponse<VNPayResponse> payCallback(HttpServletRequest request) {
        return ApiResponse.success(vnPayService.handlePaymentCallback(request));
    }
}
