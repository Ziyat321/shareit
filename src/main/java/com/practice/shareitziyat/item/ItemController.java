package com.practice.shareitziyat.item;

import com.practice.shareitziyat.item.dto.*;
import com.practice.shareitziyat.utils.RequestConstants;
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
    private final CommentMapper commentMapper;

    @PostMapping
    public ItemResponseDto create(@Valid @RequestBody ItemCreateDto itemCreate,
                                  @RequestHeader(RequestConstants.USER_HEADER) Long userId) {
        return itemMapper.toResponse(
                itemService.create(itemMapper.fromCreate(itemCreate), userId));
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
        return commentMapper.toResponse(
                itemService.createComment(commentMapper.fromCreate(commentCreate), itemId, userId)
        );
    }

    @GetMapping("/comments/{userId}")
    public List<CommentResponseDto> findCommentsByUser(@PathVariable Long userId) {
        return commentMapper.toResponse(itemService.findCommentsByUser(userId));
    }

    @GetMapping("/comments/{itemId}")
    public List<CommentResponseDto> findCommentsByItem(@PathVariable Long itemId) {
        return commentMapper.toResponse(itemService.findCommentsByItem(itemId));
    }
}

