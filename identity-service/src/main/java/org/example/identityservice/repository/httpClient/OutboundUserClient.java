package org.example.identityservice.repository.httpClient;

import org.example.identityservice.dto.response.UserInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "outbound-user-client", url = "https://www.googleapis.com")
public interface OutboundUserClient {
    @GetMapping("/oauth2/v1/userinfo")
    UserInfoResponse getUserInfo(@RequestParam String alt, @RequestParam(name = "access_token") String token);
}
