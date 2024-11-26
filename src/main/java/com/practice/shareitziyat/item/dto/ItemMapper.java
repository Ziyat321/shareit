package com.practice.shareitziyat.item.dto;

import com.practice.shareitziyat.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemMapper {

    private final CommentMapper commentMapper;

    public Item fromCreate(ItemCreateDto itemCreate) {
        Item item = new Item();
        item.setName(itemCreate.getName());
        item.setDescription(itemCreate.getDescription());
        item.setAvailable(itemCreate.getAvailable());
        return item;
    }

    public Item fromUpdate(ItemUpdateDto itemUpdate) {
        Item item = new Item();
        item.setName(itemUpdate.getName());
        item.setDescription(itemUpdate.getDescription());
        item.setAvailable(itemUpdate.getAvailable());
        return item;
    }

    public ItemResponseDto toResponse(Item item) {
        ItemResponseDto itemResponse = new ItemResponseDto();
        itemResponse.setId(item.getId());
        itemResponse.setName(item.getName());
        itemResponse.setDescription(item.getDescription());
        itemResponse.setAvailable(item.getAvailable());
        if(item.getComments() != null) {
            itemResponse.setComments(commentMapper.toResponse(item.getComments()));
        }
        return itemResponse;
    }

    public List<ItemResponseDto> toResponse(List<Item> items) {
        return items.stream()
                .map(this::toResponse)
                .toList();
    }

    public void merge(Item existingItem, Item updatedItem) {
        if (updatedItem.getName() != null) {
            existingItem.setName(updatedItem.getName());
        }
        if (updatedItem.getDescription() != null) {
            existingItem.setDescription(updatedItem.getDescription());
        }
        if (updatedItem.getAvailable() != null) {
            existingItem.setAvailable(updatedItem.getAvailable());
        }
        if (updatedItem.getOwner() != null) {
            existingItem.setOwner(updatedItem.getOwner());
        }

    }
}
