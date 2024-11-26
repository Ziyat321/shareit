package com.practice.shareitziyat.user;

import java.util.List;

public interface UserService {
    User create(User user);

    User update(User updatedUser, Long userId);

    User findById(Long userId);

    List<User> findAll();

    void deleteById(Long userId);
}
