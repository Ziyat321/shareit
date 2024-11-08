package com.practice.shareitziyat.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ItemRepository extends JpaRepository<Item, Integer> {

    @Query("select i from Item i " +
            "where (upper(i.name) like upper(concat('%', :text, '%')) " +
            "or upper(i.description) like upper(concat('%', :text, '%'))) " +
            "and i.available = true")
    List<Item> search(String text);

    List<Item> findAllByOwner_Id(int userId);
}
