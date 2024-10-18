package com.practice.shareitziyat.item;

import java.util.List;

public interface ItemRepository {
        Item create(Item item);

        Item update(Item updatedItem, int itemId);

        Item findById(int itemId);

        List<Item> findAll();

        Item search(String text);

        void deleteById(int itemId);
}
