package com.practice.shareitziyat.item;

import com.practice.shareitziyat.exceptions.NotFoundException;
import com.practice.shareitziyat.item.dto.ItemMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ItemRepositoryImpl{
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;



    public Item create(Item item) {
        itemRepository.save(item);
        return item;
    }


    public Item update(Item updatedItem, int itemId) {
        Item existingItem = findById(itemId);
        itemMapper.merge(existingItem, updatedItem);
        return existingItem;
    }


    public Item findById(int itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(()-> new NotFoundException("Item not found"));
    }


    public List<Item> findAll(int userId) {
        return itemRepository.findAll()
                .stream().filter(item -> item.getOwner().getId() == userId).toList();
    }


    public List<Item> search(String text) {
        if (StringUtils.isBlank(text)) {
            return new ArrayList<>();
        }
        // Проверка
        // если text пустой, возвращаем пустой список

        // - text может быть в названий
        // - text может быть в описаний
        // поиск по подстрокам должен быть регистронезависимым
        // находим только доступные предметы (где available == true)

        // StringUtils.containsIgnoreCase(item.getName(), text);

        return itemRepository.findAll()
                .stream().filter(Item::getAvailable).filter(item -> StringUtils.containsIgnoreCase(item.getName(), text) || StringUtils.containsIgnoreCase(item.getDescription(), text)).toList();
    }


    public void deleteById(int itemId) {
        itemRepository.deleteById(itemId);
    }
}
