package com.practice.shareitziyat.booking;

import java.util.List;

public interface BookingService {
    Booking create(Booking booking, int userId);

    Booking update(Booking booking, long bookingId, int userId, boolean approved);

    Booking findById(long bookingId, int userId);

    List<Booking> findAllByOwner(int userId, BookingState state);

    List<Booking> findAllByBooker(int userId, BookingState state);
}
