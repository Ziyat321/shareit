package com.practice.shareitziyat.item;

import com.practice.shareitziyat.exceptions.NotFoundException;
import com.practice.shareitziyat.exceptions.WrongOwnerException;
import com.practice.shareitziyat.item.dto.ItemMapper;
import com.practice.shareitziyat.user.User;
import com.practice.shareitziyat.user.UserRepository;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Data
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper itemMapper;

    @Override

    public Item create(Item item, int userId) {
        // существует ли пользователь userId
        User found = findUserById(userId);
        item.setOwner(found);
        return itemRepository.save(item);
    }

    @Override
    public Item update(Item updatedItem, int itemId, int userId) {
        // существует ли пользователь userId
        User found = findUserById(userId);
        Item existingItem = findById(itemId);
        if (existingItem.getOwner().getId() != userId) {
            throw new WrongOwnerException("Wrong owner");
        }
        // проверить, является ли данный пользователь автором предмета (статус 403)
        itemMapper.merge(existingItem, updatedItem);
        itemRepository.save(existingItem);
        return existingItem;
    }

    @Override
    public Item findById(int itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public List<Item> search(String text) {
        if (StringUtils.isBlank(text)) {
            return new ArrayList<>();
        }
        return itemRepository.search(text);
    }

    @Override
    public void deleteById(int itemId) {
        itemRepository.deleteById(itemId);
    }

    @Override
    public List<Item> findAll(int userId) {
        // существует ли пользователь userId
        findUserById(userId);
        return itemRepository.findAllByOwner_Id(userId);
    }

    @Override
    public Comment createComment(Comment comment, int itemId, int userId) {
        findUserById(userId);
        comment.getItem().setId(itemId);
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> findCommentsByUser(int userId) {
        findUserById(userId);
        return commentRepository.findAllByUser_Id(userId);
    }

    @Override
    public List<Comment> findCommentsByItem(int itemId) {
        findById(itemId);
        return commentRepository.findAllByItem_Id(itemId);
    }

    private User findUserById(int userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
    }
}
