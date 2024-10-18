package com.practice.shareitziyat.user;

import java.util.List;

public interface UserService {
    User create(User user);
    User update(User updatedUser, int userId);
    User findById(int userId);
    List<User> findAll();
    void deleteById(int userId);
}
