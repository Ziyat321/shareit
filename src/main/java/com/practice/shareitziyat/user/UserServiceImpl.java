package com.practice.shareitziyat.user;

import com.practice.shareitziyat.exceptions.NotFoundException;
import com.practice.shareitziyat.exceptions.UserAlreadyExistsException;
import com.practice.shareitziyat.user.dto.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public User create(User user) {
        checkEmail(user);
        userRepository.save(user);
        return user;
    }

    @Override
    public User update(User updatedUser, Long userId) {
        updatedUser.setId(userId);
        checkEmail(updatedUser);

        User existingUser =  findById(userId);
        userMapper.merge(existingUser, updatedUser);
        userRepository.save(existingUser);
        return existingUser;
    }

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(()-> new NotFoundException("User not found"));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteById(Long userId) {

        userRepository.deleteById(userId);
    }

    private void checkEmail(User user) {
        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
        if (optionalUser.isEmpty()) {
            return;
        }

        User found = optionalUser.get();
        if (!Objects.equals(found.getId(), user.getId())) {
            throw new UserAlreadyExistsException("Пользователь с email:" + user.getEmail() + "уже существует");
        }
    }
}
