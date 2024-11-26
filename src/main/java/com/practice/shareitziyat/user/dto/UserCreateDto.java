package com.practice.shareitziyat.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class UserCreateDto {
    @NotBlank(message = "Имя пользователя не может быть пустым")
    String name;
    @NotNull(message = "Почта пользователя не может быть пустой")
    @Email(message = "Почта пользователя не соответствует формату \"user@mail.com\"")
    String email;
}
