package com.practice.shareitziyat.server.request;

import com.practice.shareitziyat.server.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByOwnerOrderByCreatedDesc(User owner);

    Page<Request> findAllByOwnerIdNot(long ownerId, Pageable pageable);
}
