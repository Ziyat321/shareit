package com.practice.shareitziyat.server.request.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestCreateDto {
    @NotBlank(message = "Описание не может быть пустым")
    String description;

}
