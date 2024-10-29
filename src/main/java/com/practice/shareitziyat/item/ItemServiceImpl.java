package com.practice.shareitziyat.item;

import com.practice.shareitziyat.exceptions.NotFoundException;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Data
public class ItemServiceImpl implements ItemService{
    private final ItemRepository itemRepository;

    @Override
    public Item create(Item item, int userId) {
        // существует ли пользователь userId

//        item.setOwner(...);
        return itemRepository.create(item);
    }

    @Override
    public Item update(Item updatedItem, int itemId, int userId) {
        // существует ли пользователь userId

        // проверить, является ли данный пользователь автором предмета (статус 403)
        return itemRepository.update(updatedItem, itemId);
    }

    @Override
    public Item findById(int itemId) {
        return itemRepository.findById(itemId);
    }

    @Override
    public List<Item> search(String text) {
        // todo
        return new ArrayList<>();
//        Optional<Item> item =  itemRepository.search(text);
//        if(item.isEmpty()) throw new NotFoundException("Предмета с данным описанием/названием не существует");
//        else return item.get();
    }

    @Override
    public void deleteById(int itemId) {
        itemRepository.deleteById(itemId);
    }

    @Override
    public List<Item> findAll(int userId) {
        // существует ли пользователь userId

        return itemRepository.findAll(userId);
    }
}
