package com.practice.shareitziyat.server.request.dto;

import com.practice.shareitziyat.server.item.dto.ItemResponseDto;
import com.practice.shareitziyat.server.user.dto.UserResponseDto;
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
