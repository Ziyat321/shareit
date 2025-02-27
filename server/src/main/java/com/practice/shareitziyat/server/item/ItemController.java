package com.practice.shareitziyat.server.item;

import com.practice.shareitziyat.server.item.dto.*;
import com.practice.shareitziyat.server.utils.RequestConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @PostMapping
    public ItemResponseDto create(@Valid @RequestBody ItemCreateDto itemCreate,
                                  @RequestHeader(RequestConstants.USER_HEADER) Long userId,
                                  @RequestParam(required = false) Long requestId) {
        return itemMapper.toResponse(
                itemService.create(itemMapper.fromCreate(itemCreate), userId, requestId));
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto update(@PathVariable Long itemId,
                                  @RequestBody ItemUpdateDto itemUpdate,
                                  @RequestHeader(RequestConstants.USER_HEADER) Long userId) {
        return itemMapper.toResponse(
                itemService.update(itemMapper.fromUpdate(itemUpdate), itemId, userId)
        );
    }

    @GetMapping
    public List<ItemResponseDto> findAll(@RequestHeader(RequestConstants.USER_HEADER) Long userId) {
        return itemMapper.toResponse(itemService.findAll(userId));
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto findById(@PathVariable Long itemId) {
        return itemMapper.toResponse(itemService.findById(itemId));
    }

    @DeleteMapping("{itemId}")
    public void deleteById(@PathVariable Long itemId) {
        itemService.deleteById(itemId);
    }

    @GetMapping("/search")
    public List<ItemResponseDto> search(@RequestParam String text) {
        return itemMapper.toResponse(itemService.search(text));
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto createComment(@PathVariable Long itemId,
                                            @Valid @RequestBody CommentCreateDto commentCreate,
                                            @RequestHeader(RequestConstants.USER_HEADER) Long userId) {
        return itemMapper.toResponseComment(
                itemService.createComment(itemMapper.fromCreateComment(commentCreate), itemId, userId)
        );
    }

    @GetMapping("/comments/users/{userId}")
    public List<CommentResponseDto> findCommentsByUser(@PathVariable Long userId) {
        return itemMapper.toResponseComment(itemService.findCommentsByUser(userId));
    }

    @GetMapping("/comments/items/{itemId}")
    public List<CommentResponseDto> findCommentsByItem(@PathVariable Long itemId) {
        return itemMapper.toResponseComment(itemService.findCommentsByItem(itemId));
    }
}

