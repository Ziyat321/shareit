package com.practice.shareitziyat.user;

public interface UserService {
    User create(User user);
    User update(User updatedUser, int userId);
    User findById(int userId);
    List<User> findAll();
    void deleteById(int userId);
}
