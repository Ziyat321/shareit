package com.practice.shareitziyat.user;

import com.practice.shareitziyat.exceptions.UserAlreadyExistsException;
import com.practice.shareitziyat.user.dto.UserMapper;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Data
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public User create(User user) {
        checkEmail(user);
        return userRepository.create(user);
    }

    @Override
    public User update(User updatedUser, int userId) {
        updatedUser.setId(userId);
        checkEmail(updatedUser);

        User existingUser = userRepository.findById(userId);
        userMapper.merge(existingUser, updatedUser);
        return existingUser;
    }

    @Override
    public User findById(int userId) {
        return userRepository.findById(userId);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteById(int userId) {
        userRepository.deleteById(userId);
    }

    private void checkEmail(User user) {
        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
        if (optionalUser.isEmpty()) {
            return;
        }

        User found = optionalUser.get();
        if (found.getId() != user.getId()) {
            throw new UserAlreadyExistsException("Пользователь с email:" + user.getEmail() + "уже существует");
        }
    }
}
