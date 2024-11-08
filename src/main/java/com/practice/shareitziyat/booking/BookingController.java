package com.practice.shareitziyat.booking;

import com.practice.shareitziyat.booking.dto.BookingCreateDto;
import com.practice.shareitziyat.booking.dto.BookingResponseDto;
import com.practice.shareitziyat.utils.RequestConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("bookings/")
public class BookingController {
    @PostMapping
    public BookingResponseDto create(@Valid @RequestBody BookingCreateDto bookingCreate,
                                     @RequestHeader(RequestConstants.USER_HEADER) int userId){
            return null;
    }
}
