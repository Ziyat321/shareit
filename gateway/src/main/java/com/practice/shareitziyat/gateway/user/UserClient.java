package com.practice.shareitziyat.gateway.user;

import com.practice.shareitziyat.server.user.dto.UserCreateDto;
import com.practice.shareitziyat.server.user.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class UserClient {
    private final RestTemplate restTemplate;
    @Value("${server.url}")
    private String serverUrl;

    public UserResponseDto create(UserCreateDto userCreate) {
        HttpEntity<UserCreateDto> request = new HttpEntity<>(userCreate);
        return restTemplate.exchange(serverUrl + "/users", HttpMethod.POST, request, UserResponseDto.class)
                .getBody();

    }
}
