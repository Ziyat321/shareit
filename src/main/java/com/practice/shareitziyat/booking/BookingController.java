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
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

    @PostMapping
    public BookingResponseDto create(@Valid @RequestBody BookingCreateDto bookingCreate,
                                     @RequestHeader(RequestConstants.USER_HEADER) Long userId){
            return bookingMapper.toResponse(
                    bookingService.create(bookingMapper.fromCreate(bookingCreate), userId))
                    ;
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto update ( @PathVariable int bookingId,
                                      @RequestParam boolean approved,
                                      @RequestHeader(RequestConstants.USER_HEADER) Long userId) {
        return bookingMapper.toResponse(
                bookingService.update( bookingId, userId, approved)) ;

    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto findById(@PathVariable int bookingId,
                                       @RequestHeader(RequestConstants.USER_HEADER) Long userId) {
        return bookingMapper.toResponse(
                bookingService.findById(bookingId, userId));
    }

    @GetMapping
    public List<BookingResponseDto> findAllByBooker(@RequestParam(defaultValue = "ALL") BookingState state,
                                                    @RequestHeader(RequestConstants.USER_HEADER) Long userId){
        return bookingMapper.toResponse(
                bookingService.findAllByBooker(userId, state)
        );
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> findAllByOwner(@RequestParam(defaultValue = "ALL") BookingState state,
                                                   @RequestHeader(RequestConstants.USER_HEADER) Long ownerId){
        return bookingMapper.toResponse(
                bookingService.findAllByOwner(ownerId, state)
        );
    }
}
