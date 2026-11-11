package org.example.productservice.controller;

import java.util.List;
import java.util.Set;

import org.example.productservice.dto.response.ApiResponse;
import org.example.productservice.dto.response.ProductResponse;
import org.example.productservice.service.ProductService;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/internal/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalProductController {
    ProductService productService;

    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getProductById(@PathVariable String id) {
        return ApiResponse.success(productService.getProduct(id));
    }

    @GetMapping
    public ApiResponse<List<ProductResponse>> getProductByIds(@RequestParam Set<String> productIds) {
        return ApiResponse.success(productService.getProductByIds(productIds));
    }
}
