package com.practice.shareitziyat.gateway.item;

import com.practice.shareitziyat.server.item.dto.ItemCreateDto;
import com.practice.shareitziyat.server.item.dto.ItemResponseDto;
import com.practice.shareitziyat.server.utils.RequestConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class ItemClient {
    private final RestTemplate restTemplate;
    @Value("${server.url}")
    private String serverUrl;

    public ItemResponseDto create(ItemCreateDto itemCreate, long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(RequestConstants.USER_HEADER, String.valueOf(userId));
        HttpEntity<ItemCreateDto> request = new HttpEntity<>(itemCreate, headers);
        Map<String, Object> params = Map.of("asdasd", 23);
        return restTemplate.exchange(serverUrl + "/items", HttpMethod.POST, request, ItemResponseDto.class, params)
                .getBody();

    }

    private <T, R> R post(T entity, String url) {
        HttpEntity<T> request = new HttpEntity<>(entity);
        return (R) restTemplate.exchange(url, HttpMethod.POST, request, Object.class).getBody();
    }

    private <T, R> R post(T entity, String url, long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(RequestConstants.USER_HEADER, String.valueOf(userId));
        HttpEntity<T> request = new HttpEntity<>(entity, headers);
        return (R) restTemplate.exchange(url, HttpMethod.POST, request, Object.class).getBody();
    }

//    private <T> T post(T entity, String url, int id) {
//        HttpHeaders headers = new HttpHeaders();
//    }
}
