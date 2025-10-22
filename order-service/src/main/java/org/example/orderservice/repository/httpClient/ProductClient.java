package org.example.orderservice.repository.httpClient;

import java.util.List;
import java.util.Set;

import org.example.orderservice.dto.response.ApiResponse;
import org.example.orderservice.dto.response.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-service", url = "${app.services.product}")
public interface ProductClient {
    @GetMapping("/internal/products")
    ApiResponse<List<ProductResponse>> getProductByIds(@RequestParam Set<String> productIds);
}
