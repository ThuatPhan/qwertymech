package org.example.orderservice.repository.httpClient;

import org.example.orderservice.config.AuthenticationRequestInterceptor;
import org.example.orderservice.dto.response.ApiResponse;
import org.example.orderservice.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "identity-service",
        url = "${app.services.identity}",
        configuration = {AuthenticationRequestInterceptor.class})
public interface IdentityClient {
    @GetMapping("/internal/users/my-info")
    ApiResponse<UserResponse> getUser();
}
