package com.practice.shareitziyat.gateway.user;

import com.practice.shareitziyat.gateway.user.UserClient;
import com.practice.shareitziyat.server.user.dto.UserCreateDto;
import com.practice.shareitziyat.server.user.dto.UserUpdateDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/users")
public class UserController {

    private final UserClient client;

    @GetMapping
    public ResponseEntity<Object> findAll() {
        return client.findAll();
    }

    @GetMapping("{/userId}")
    public ResponseEntity<Object> findById(@PathVariable Long userId) {
        return client.findById(userId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid UserCreateDto userCreate) {
        return client.create(userCreate);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@PathVariable Long userId,
                                         @RequestBody @Valid UserUpdateDto userUpdate) {
        return client.update(userId, userUpdate);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> delete(@PathVariable Long userId) {
        return client.delete(userId);
    }
}