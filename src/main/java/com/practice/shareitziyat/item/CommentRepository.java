package com.practice.shareitziyat.item;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByUser_Id(long userId);

    List<Comment> findAllByItem_Id(long itemId);
}
