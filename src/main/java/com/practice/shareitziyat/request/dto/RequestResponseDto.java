package com.practice.shareitziyat.request.dto;

import com.practice.shareitziyat.item.Item;
import com.practice.shareitziyat.item.dto.ItemResponseDto;
import com.practice.shareitziyat.user.User;
import com.practice.shareitziyat.user.dto.UserResponseDto;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestResponseDto {
    Long id;

    String description;

    LocalDateTime created;

    UserResponseDto owner;

    List<ItemResponseDto> items;
}
