package org.example.cartservice.controller;

import jakarta.validation.Valid;

import org.example.cartservice.dto.request.CartItemCreationRequest;
import org.example.cartservice.dto.response.ApiResponse;
import org.example.cartservice.dto.response.CartItemResponse;
import org.example.cartservice.dto.response.PageResponse;
import org.example.cartservice.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {
    CartService cartService;

    @PostMapping("/add-item")
    public ApiResponse<CartItemResponse> addCartItem(@RequestBody @Valid CartItemCreationRequest request) {
        return ApiResponse.success(cartService.addCartItem(request));
    }

    @GetMapping("/items")
    public ApiResponse<PageResponse<CartItemResponse>> getCartItems(
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "2") int size) {
        return ApiResponse.success(cartService.getCartItems(page, size));
    }

    @PutMapping("/items/{id}")
    public ApiResponse<CartItemResponse> updateItemQuantity(@PathVariable String id, @RequestParam int quantity) {
        return ApiResponse.success(cartService.updateItemQuantity(id, quantity));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/items/{id}")
    public void deleteItem(@PathVariable String id) {
        cartService.deleteItem(id);
    }
}
