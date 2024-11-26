package com.practice.shareitziyat.request;

import com.practice.shareitziyat.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByOwnerOrderByCreatedDesc(User owner);
}
