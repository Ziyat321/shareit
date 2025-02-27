package com.practice.shareitziyat.server.item.dto;

import com.practice.shareitziyat.server.booking.dto.BookingResponseDto;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemResponseDto {
    Long id;
    String name;
    String description;
    Boolean available;
    BookingResponseDto lastBooking;
    BookingResponseDto nextBooking;
    List<CommentResponseDto> comments;
    Long requestId;
}
