package com.practice.shareitziyat.gateway.item;

import com.practice.shareitziyat.server.item.dto.ItemCreateDto;
import com.practice.shareitziyat.server.item.dto.ItemResponseDto;
import com.practice.shareitziyat.server.utils.RequestConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ItemResponseDto create(@Valid @RequestBody ItemCreateDto itemCreate,
                                  @RequestHeader(RequestConstants.USER_HEADER) Long userId,
                                  @RequestParam(required = false) Long requestId) {
        return itemClient.create(itemCreate, userId);
    }
}
