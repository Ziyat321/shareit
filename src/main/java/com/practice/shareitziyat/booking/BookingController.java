package com.practice.shareitziyat.booking;

import com.practice.shareitziyat.booking.dto.BookingCreateDto;
import com.practice.shareitziyat.booking.dto.BookingMapper;
import com.practice.shareitziyat.booking.dto.BookingResponseDto;
import com.practice.shareitziyat.utils.RequestConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("bookings/")
public class BookingController {
    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

    @PostMapping
    public BookingResponseDto create(@Valid @RequestBody BookingCreateDto bookingCreate,
                                     @RequestHeader(RequestConstants.USER_HEADER) int userId){
            return bookingMapper.toResponse(
                    bookingService.create(bookingMapper.fromCreate(bookingCreate), userId))
                    ;
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto update (@Valid @RequestBody BookingCreateDto bookingCreateDto,
                                      @PathVariable int bookingId,
                                      @RequestParam boolean approved,
                                      @RequestHeader(RequestConstants.USER_HEADER) int userId) {
        return bookingMapper.toResponse(
                bookingService.update(bookingMapper.fromCreate(bookingCreateDto), bookingId, userId, approved))
                ;

    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto findById(@PathVariable int bookingId,
                                       @RequestHeader(RequestConstants.USER_HEADER) int userId) {
        return bookingMapper.toResponse(
                bookingService.findById(bookingId, userId));
    }

    @GetMapping("/bookings")
    public List<BookingResponseDto> findAllByBooker(@RequestParam(defaultValue = "ALL") BookingState state,
                                                    @RequestHeader(RequestConstants.USER_HEADER) int userId){

    }
}
