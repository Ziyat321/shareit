package com.practice.shareitziyat.gateway.user;

import com.practice.shareitziyat.server.user.dto.UserCreateDto;
import com.practice.shareitziyat.server.user.dto.UserResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public UserResponseDto create(@Valid @RequestBody UserCreateDto userCreate) {
        System.out.println(userCreate);
        UserResponseDto userResponseDto = userClient.create(userCreate);
        System.out.println(userResponseDto);
        return userResponseDto;
    }
}
