package com.practice.shareitziyat.server.booking;

import com.practice.shareitziyat.server.exceptions.BadRequestException;

public enum BookingState {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED,
    ELSE;

    public static BookingState of(String str) {
        for (BookingState value : values()) {
            if (value.name().equals(str)) {
                return value;
            }
        }
        throw new BadRequestException("Unknown state: " + str);
    }
}
