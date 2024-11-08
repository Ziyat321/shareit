package com.practice.shareitziyat.booking.dto;


import com.practice.shareitziyat.booking.BookingStatus;
import com.practice.shareitziyat.item.dto.ItemResponseDto;
import com.practice.shareitziyat.user.dto.UserResponseDto;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingResponseDto {
    LocalDateTime start;

    LocalDateTime end;

    BookingStatus status;

    UserResponseDto booker;

    ItemResponseDto item;
}
