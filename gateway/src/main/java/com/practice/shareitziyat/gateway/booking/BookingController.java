package com.practice.shareitziyat.gateway.booking;

import com.practice.shareitziyat.server.booking.BookingState;
import com.practice.shareitziyat.server.booking.dto.BookingCreateDto;
import com.practice.shareitziyat.server.exceptions.BadRequestException;
import com.practice.shareitziyat.server.exceptions.ErrorResponse;
import com.practice.shareitziyat.server.utils.RequestConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody BookingCreateDto bookingCreate,
                                         @RequestHeader(RequestConstants.USER_HEADER) Long userId) {
        return bookingClient.create(bookingCreate, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> update(@PathVariable int bookingId,
                                         @RequestParam boolean approved,
                                         @RequestHeader(RequestConstants.USER_HEADER) Long userId) {
        return bookingClient.update(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findById(@PathVariable int bookingId,
                                           @RequestHeader(RequestConstants.USER_HEADER) Long userId) {
        return bookingClient.findById(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllByBooker(@RequestParam(defaultValue = "ALL") String state,
                                                  @RequestHeader(RequestConstants.USER_HEADER) Long userId) {
        BookingState bookingState = BookingState.of(state);
        return bookingClient.findAllByBooker(bookingState, userId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findAllByOwner(@RequestParam(defaultValue = "ALL") String state,
                                                 @RequestHeader(RequestConstants.USER_HEADER) Long ownerId) {
        BookingState bookingState = BookingState.of(state);
        return bookingClient.findAllByOwner(bookingState, ownerId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(BadRequestException e) {
        return new ErrorResponse(e.getMessage(), null);
    }
}
