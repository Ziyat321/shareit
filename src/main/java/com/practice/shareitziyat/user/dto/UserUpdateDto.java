package com.practice.shareitziyat.user.dto;

import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateDto {
    String name;

    @Email(message = "Почта пользователя не соответсвует формату \"user@mail.com\"")
    String email;
}
