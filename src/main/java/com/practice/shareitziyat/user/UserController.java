package com.practice.shareitziyat.user;

import com.practice.shareitziyat.user.dto.UserCreateDto;
import com.practice.shareitziyat.user.dto.UserMapper;
import com.practice.shareitziyat.user.dto.UserResponseDto;
import com.practice.shareitziyat.user.dto.UserUpdateDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public UserResponseDto create(@Valid @RequestBody UserCreateDto userCreate) {
        return userMapper.toResponse(
                userService.create(userMapper.fromCreate(userCreate)));
    }

    @PatchMapping("/{userId}")
    public UserResponseDto update(
            @PathVariable int userId,
            @Valid @RequestBody UserUpdateDto userUpdate) {
        log.debug("PATCH /users/{}", userId);
        return userMapper.toResponse(
                userService.update(userMapper.fromUpdate(userUpdate), userId)
        );
    }

    @GetMapping
    public List<UserResponseDto> findAll() {
        return userMapper.toResponse(userService.findAll());
    }

    @GetMapping("/{userId}")
    public UserResponseDto findById(@PathVariable int userId) {
        return userMapper.toResponse(userService.findById(userId));
    }

    @DeleteMapping("/{userId}")
    public void deleteById(@PathVariable int userId) {
        userService.deleteById(userId);
    }
}
