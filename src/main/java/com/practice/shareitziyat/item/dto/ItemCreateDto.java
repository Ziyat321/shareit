package com.practice.shareitziyat.item.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemCreateDto {
    @NotBlank(message = "Название предмета не может быть пустым")
    String name;
    @NotBlank(message = "Описание не может быть пустым")
    String description;
    @NotNull
    Boolean available;

}
