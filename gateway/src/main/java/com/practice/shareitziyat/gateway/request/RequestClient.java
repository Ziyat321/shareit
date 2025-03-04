package com.practice.shareitziyat.gateway.request;

import com.practice.shareitziyat.gateway.config.BaseClient;
import com.practice.shareitziyat.server.request.dto.RequestCreateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Map;

@Service
public class RequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";
    @Autowired
    public RequestClient(@Value("${server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory.class)
                        .build()
        );
    }

    public ResponseEntity<Object> create(RequestCreateDto requestCreate, long userId) {
        return post("", userId, requestCreate);
    }

    public ResponseEntity<Object> findAllByUser(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> findAll(int from, int size) {
        return get("/all", null,
                Map.of("from", Integer.toString(from), "size", Integer.toString(size)));
    }

    public ResponseEntity<Object> findById(long requestId) {
        return get("/{requestId}", null, Map.of("requestId", Long.toString(requestId)));
    }
}
