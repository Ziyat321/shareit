package com.practice.shareitziyat.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface UserRepository {
    User create (User user);



    User findById(int userId);

    List<User> findAll();

    void deleteById(int userId);

    Optional<User> findByEmail(String email);
}
