package com.practice.shareitziyat.item;

import com.practice.shareitziyat.item.dto.ItemCreateDto;
import com.practice.shareitziyat.item.dto.ItemMapper;
import com.practice.shareitziyat.item.dto.ItemResponseDto;
import com.practice.shareitziyat.item.dto.ItemUpdateDto;
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

    @PostMapping
    public ItemResponseDto create(@Valid @RequestBody ItemCreateDto itemCreate,
                                  @RequestHeader(RequestConstants.USER_HEADER) int userId){
        return itemMapper.toResponse(
                itemService.create(itemMapper.fromCreate(itemCreate), userId));
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto update (@PathVariable int itemId,
                                   @RequestBody ItemUpdateDto itemUpdate,
                                   @RequestHeader(RequestConstants.USER_HEADER) int userId){
        return itemMapper.toResponse(
                itemService.update(itemMapper.fromUpdate(itemUpdate), itemId, userId)
        );
    }

    @GetMapping
    public List<ItemResponseDto> findAll(@RequestHeader(RequestConstants.USER_HEADER) int userId){
        return itemMapper.toResponse(itemService.findAll(userId));
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto findById(@PathVariable int itemId){
        return itemMapper.toResponse(itemService.findById(itemId));
    }

    @DeleteMapping("{itemId}")
    public void deleteById(@PathVariable int itemId){
        itemService.deleteById(itemId);
    }

//    @GetMapping
//    public ItemResponseDto search(@RequestParam String text){
//        return itemMapper.toResponse(itemService.search(text));
//    }
}
