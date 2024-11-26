package com.practice.shareitziyat.booking;

import java.util.List;

public interface BookingService {
    Booking create(Booking booking, Long userId);

    Booking update( long bookingId, Long userId, boolean approved);

    Booking findById(long bookingId, Long userId);

    List<Booking> findAllByOwner(Long userId, BookingState state);

    List<Booking> findAllByBooker(Long userId, BookingState state);
}
