package com.practice.shareitziyat.item;

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
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Integer, Item> itemMap = new HashMap<>();
    private final ItemMapper itemMapper;

    private int uniqueId = 1;

    @Override
    public Item create(Item item) {
        item.setId(uniqueId++);
        itemMap.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Item updatedItem, int itemId) {
        Item existingItem = itemMap.get(itemId);
        itemMapper.merge(existingItem, updatedItem);
        return existingItem;
    }

    @Override
    public Item findById(int itemId) {
        return itemMap.get(itemId);
    }

    @Override
    public List<Item> findAll(int userId) {
        return itemMap.values().stream().filter(item -> item.getOwner().getId() == userId).toList();
    }

    @Override
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

        return itemMap.values().stream().filter(Item::getAvailable).filter(item -> StringUtils.containsIgnoreCase(item.getName(), text) || StringUtils.containsIgnoreCase(item.getDescription(), text)).toList();
    }

    @Override
    public void deleteById(int itemId) {
        itemMap.remove(itemId);
    }
}
