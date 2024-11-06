package com.practice.shareitziyat.user;

import com.practice.shareitziyat.exceptions.NotFoundException;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Data
public class UserRepositoryImpl{
    private final UserRepository userRepository;


    public User create(User user) {
        userRepository.save(user);
        return user;
    }




    public User findById(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(()-> new NotFoundException("User not found"));
    }


    public List<User> findAll() {
        return userRepository.findAll();
    }


    public void deleteById(int userId) {
        userRepository.deleteById(userId);
    }


    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
