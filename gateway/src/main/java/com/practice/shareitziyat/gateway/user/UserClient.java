package com.practice.shareitziyat.gateway.user;

import com.practice.shareitziyat.gateway.config.BaseClient;
import com.practice.shareitziyat.server.user.dto.UserCreateDto;
import com.practice.shareitziyat.server.user.dto.UserUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Map;

@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
//                        .requestFactory(HttpComponentsClientHttpRequestFactory.class)
                        .build()
        );
    }

    public ResponseEntity<Object> findAll() {
        return get("");
    }

    public ResponseEntity<Object> findById(long userId) {
        return get("/{userId}", null, Map.of("userId", Long.toString(userId)));
    }

    public ResponseEntity<Object> create(UserCreateDto userCreate) {
        return post("", userCreate);
    }

    public ResponseEntity<Object> update(long userId, UserUpdateDto userUpdate) {
        return patch("/{userId}", null, Map.of("userId", Long.toString(userId)), userUpdate);
    }

    public ResponseEntity<Object> delete(long userId) {
        return delete("/{userId}", null, Map.of("userId", Long.toString(userId)));
    }
}