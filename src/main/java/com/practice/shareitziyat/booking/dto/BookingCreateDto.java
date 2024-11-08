package com.practice.shareitziyat.booking.dto;


import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingCreateDto {
    Long itemId;

    @FutureOrPresent
    @NotNull
    LocalDateTime start;

    @Future
    @NotNull
    LocalDateTime end;
}
