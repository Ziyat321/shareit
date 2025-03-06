package com.practice.shareitziyat.gateway.item;

import com.practice.shareitziyat.gateway.config.BaseClient;
import com.practice.shareitziyat.server.item.dto.CommentCreateDto;
import com.practice.shareitziyat.server.item.dto.ItemCreateDto;
import com.practice.shareitziyat.server.item.dto.ItemUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
//                        .requestFactory(SimpleClientHttpRequestFactory.class)
                        .build()
        );
    }

    public ResponseEntity<Object> create(ItemCreateDto itemCreateDto, long userId) {
        return post("", userId, itemCreateDto);
    }

    public ResponseEntity<Object> update(long itemId, ItemUpdateDto itemUpdate, long userId) {
        return patch("/{itemId}", userId, Map.of("itemId", Long.toString(itemId)), itemUpdate);
    }

    public ResponseEntity<Object> findAll(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> findById(long itemId) {
        return get("/{itemId}", null, Map.of("itemId", Long.toString(itemId)));
    }

    public ResponseEntity<Object> deleteById(long itemId) {
        return delete("/{itemId}", null, Map.of("itemId", Long.toString(itemId)));
    }

    public ResponseEntity<Object> search(String text) {
        return get("/search?text={text}", null, Map.of("text", text));
    }

    public ResponseEntity<Object> createComment(long itemId, CommentCreateDto commentCreate, long userId) {
        return post("/{itemId}/comment", userId, Map.of("itemId", Long.toString(itemId)), commentCreate);
    }

    public ResponseEntity<Object> findCommentsByUser(long userId) {
        return get("/comments/users/{userId}", null, Map.of("userId", Long.toString(userId)));
    }

    public ResponseEntity<Object> findCommentsByItem(long itemId) {
        return get("/comments/items/{itemId}", null, Map.of("itemId", Long.toString(itemId)));
    }
}
