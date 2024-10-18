package com.practice.shareitziyat.user;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Data
public class UserRepositoryImpl implements UserRepository {
    private final Map<Integer, User> userMap = new HashMap<>();
    private int uniqueId = 1;

    @Override
    public User create(User user) {
        user.setId(uniqueId++);
        userMap.put(user.getId(), user);
        return user;
    }



    @Override
    public User findById(int userId) {
        return userMap.get(userId);
    }

    @Override
    public List<User> findAll() {
        return userMap.values().stream().toList();
    }

    @Override
    public void deleteById(int userId) {
        userMap.remove(userId);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userMap.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }
}
