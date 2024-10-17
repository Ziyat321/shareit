package com.practice.shareitziyat.user;

import com.practice.shareitziyat.user.dto.UserCreateDto;
import com.practice.shareitziyat.user.dto.UserMapper;
import com.practice.shareitziyat.user.dto.UserResponseDto;
import com.practice.shareitziyat.user.dto.UserUpdateDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    // POST /users
    // PATCH /users/{userId}
    // GET /users/{userId}
    // GET /users
    // DELETE /users/{userId}
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public UserResponseDto create (@Valid @RequestBody UserCreateDto userCreate){
        return userMapper.toResponse(
                userService.create(userMapper.fromCreate(userCreate)));
    }

    @PatchMapping("/{userId}")
    public UserResponseDto update (
            @PathVariable int userId,
            @Valid @RequestBody UserUpdateDto userUpdate){
        return userMapper.toResponse(
                userService.update(userMapper.fromUpdate(userUpdate), userId)
        );
    }
}
