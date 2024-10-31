package com.practice.shareitziyat.item;

import com.practice.shareitziyat.exceptions.NotFoundException;
import com.practice.shareitziyat.exceptions.WrongOwnerException;
import com.practice.shareitziyat.user.UserRepository;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public Item create(Item item, int userId) {
        // существует ли пользователь userId
        if (userRepository.findById(userId) == null) {
            throw new NotFoundException("User not found");
        }
        item.setOwner(userRepository.findById(userId));
        return itemRepository.create(item);
    }

    @Override
    public Item update(Item updatedItem, int itemId, int userId) {
        // существует ли пользователь userId
        if (userRepository.findById(userId) == null) {
            throw new NotFoundException("User not found");
        }
        if (itemRepository.findById(itemId).getOwner().getId() != userId) {
            throw new WrongOwnerException("Wrong owner");
        }
        // проверить, является ли данный пользователь автором предмета (статус 403)
        return itemRepository.update(updatedItem, itemId);
    }

    @Override
    public Item findById(int itemId) {
        return itemRepository.findById(itemId);
    }

    @Override
    public List<Item> search(String text) {
        return itemRepository.search(text);
    }

    @Override
    public void deleteById(int itemId) {
        itemRepository.deleteById(itemId);
    }

    @Override
    public List<Item> findAll(int userId) {
        // существует ли пользователь userId
        if (userRepository.findById(userId) == null) {
            throw new NotFoundException("User not found");
        }
        return itemRepository.findAll(userId);
    }
}
