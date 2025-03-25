package com.practice.shareitziyat.server.request;

import java.util.List;

public interface RequestService {
    Request create (Request request, Long userId);

    List<Request> findAllByUser(Long userId);

    List<Request> findAll(Long userId, int from, int size);

    Request findById(Long userId, Long requestId);
}
