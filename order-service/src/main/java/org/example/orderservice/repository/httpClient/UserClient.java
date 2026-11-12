package org.example.orderservice.repository.httpClient;

import org.example.common.dto.ApiResponse;
import org.example.orderservice.config.AuthenticationRequestInterceptor;
import org.example.orderservice.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "user-service",
        url = "${app.services.user}",
        configuration = {AuthenticationRequestInterceptor.class})
public interface UserClient {
    @GetMapping("/internal/users/my-info")
    ApiResponse<UserResponse> getUser();
}
