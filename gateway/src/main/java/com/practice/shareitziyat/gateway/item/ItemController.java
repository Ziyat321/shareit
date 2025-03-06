package com.practice.shareitziyat.gateway.item;

import com.practice.shareitziyat.server.item.dto.*;
import com.practice.shareitziyat.server.utils.RequestConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody ItemCreateDto itemCreate,
                                         @RequestHeader(RequestConstants.USER_HEADER) Long userId) {
        return itemClient.create(itemCreate, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@PathVariable Long itemId,
                                         @RequestBody ItemUpdateDto itemUpdate,
                                         @RequestHeader(RequestConstants.USER_HEADER) Long userId) {
        return itemClient.update(itemId, itemUpdate, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAll(@RequestHeader(RequestConstants.USER_HEADER) Long userId) {
        return itemClient.findAll(userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findById(@PathVariable Long itemId) {
        return itemClient.findById(itemId);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteById(@PathVariable Long itemId) {
        return itemClient.deleteById(itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam String text) {
        return itemClient.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@PathVariable Long itemId,
                                                @Valid @RequestBody CommentCreateDto commentCreate,
                                                @RequestHeader(RequestConstants.USER_HEADER) Long userId) {
        return itemClient.createComment(itemId, commentCreate, userId);
    }

    @GetMapping("/comments/users/{userId}")
    public ResponseEntity<Object> findCommentsByUser(@PathVariable Long userId) {
        return itemClient.findCommentsByUser(userId);
    }

    @GetMapping("/comments/items/{itemId}")
    public ResponseEntity<Object> findCommentsByItem(@PathVariable Long itemId) {
        return itemClient.findCommentsByItem(itemId);
    }
}
