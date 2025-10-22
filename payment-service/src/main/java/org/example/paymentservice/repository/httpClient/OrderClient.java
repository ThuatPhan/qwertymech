package org.example.paymentservice.repository.httpClient;

import org.example.paymentservice.dto.response.ApiResponse;
import org.example.paymentservice.dto.response.OrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service", url = "${app.services.order}")
public interface OrderClient {
    @GetMapping("/order/internal/order/{id}")
    ApiResponse<OrderResponse> getOrder(@PathVariable Long id);
}
