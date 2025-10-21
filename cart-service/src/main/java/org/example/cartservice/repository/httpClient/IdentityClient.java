package org.example.cartservice.repository.httpClient;

import org.example.cartservice.config.AuthenticationRequestInterceptor;
import org.example.cartservice.dto.response.ApiResponse;
import org.example.cartservice.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "identity-service",
        url = "${app.services.identity}",
        configuration = {AuthenticationRequestInterceptor.class})
public interface IdentityClient {
    @GetMapping("/internal/users/info")
    ApiResponse<UserResponse> getUser();
}
