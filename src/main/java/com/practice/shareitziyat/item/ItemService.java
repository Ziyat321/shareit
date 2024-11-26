package com.practice.shareitziyat.item;

import java.util.List;

public interface ItemService {
    Item create(Item item, Long userId);

    Item update(Item updatedItem, Long itemId, Long userId);

    Item findById(Long itemId);

    List<Item> search(String text);

    void deleteById(Long itemId);

    List<Item> findAll(Long userId);

    Comment createComment(Comment comment, Long itemId, Long userId);

    List<Comment> findCommentsByUser(Long userId);

    List<Comment> findCommentsByItem(Long itemId);
}
