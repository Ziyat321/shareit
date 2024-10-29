package com.practice.shareitziyat.item.dto;

import com.practice.shareitziyat.item.Item;
import com.practice.shareitziyat.user.dto.UserResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemMapper {
    public Item fromCreate(ItemCreateDto itemCreate){
        Item item = new Item();
        item.setName(itemCreate.getName());
        item.setDescription(itemCreate.getDescription());
        item.setAvailable(itemCreate.getAvailable());
        return item;
    }

    public Item fromUpdate(ItemUpdateDto itemUpdate){
        Item item = new Item();
        item.setName(itemUpdate.getName());
        item.setDescription(itemUpdate.getDescription());
        item.setAvailable(itemUpdate.getAvailable());
        return item;
    }

    public ItemResponseDto toResponse(Item item){
        ItemResponseDto itemResponse = new ItemResponseDto();
        itemResponse.setId(item.getId());
        itemResponse.setName(item.getName());
        itemResponse.setDescription(item.getDescription());
        itemResponse.setAvailable(itemResponse.isAvailable());
        return itemResponse;
    }

    public List<ItemResponseDto> toResponse(List<Item> items){
        return items.stream()
                .map(this::toResponse)
                .toList();
    }

    public void merge(Item existingItem, Item updatedItem){
        if(updatedItem.getName() != null){
            existingItem.setName(updatedItem.getName());
        }
        if(updatedItem.getDescription() != null){
            existingItem.setDescription(updatedItem.getDescription());
        }
        existingItem.setAvailable(updatedItem.isAvailable());
        if(updatedItem.getOwner() != null){
            existingItem.setOwner(updatedItem.getOwner());
        }

    }
}
