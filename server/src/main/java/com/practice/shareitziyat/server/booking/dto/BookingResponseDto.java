package com.practice.shareitziyat.server.booking.dto;


import com.practice.shareitziyat.server.booking.BookingStatus;
import com.practice.shareitziyat.server.item.dto.ItemResponseDto;
import com.practice.shareitziyat.server.user.dto.UserResponseDto;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingResponseDto {
    Long id;

    LocalDateTime start;

    LocalDateTime end;

    BookingStatus status;

    UserResponseDto booker;

    ItemResponseDto item;
}
