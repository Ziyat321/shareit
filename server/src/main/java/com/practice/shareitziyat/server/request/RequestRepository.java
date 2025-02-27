package com.practice.shareitziyat.server.request;

import com.practice.shareitziyat.server.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByOwnerOrderByCreatedDesc(User owner);
}
