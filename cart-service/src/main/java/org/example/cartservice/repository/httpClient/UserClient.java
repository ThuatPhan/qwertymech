package org.example.cartservice.repository.httpClient;

import org.example.cartservice.config.AuthenticationRequestInterceptor;
import org.example.cartservice.dto.response.UserResponse;
import org.example.common.dto.ApiResponse;
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
