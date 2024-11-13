package com.practice.shareitziyat.item;

import java.util.List;

public interface ItemService {
    Item create(Item item, int userId);

    Item update(Item updatedItem, int itemId, int userId);

    Item findById(int itemId);

    List<Item> search(String text);

    void deleteById(int itemId);

    List<Item> findAll(int userId);

    Comment createComment(Comment comment, int itemId, int userId);

    List<Comment> findCommentsByUser(int userId);

    List<Comment> findCommentsByItem(int itemId);
}
