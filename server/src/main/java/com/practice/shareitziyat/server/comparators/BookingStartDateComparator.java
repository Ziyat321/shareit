package com.practice.shareitziyat.server.comparators;

import com.practice.shareitziyat.server.booking.Booking;

import java.util.Comparator;

public class BookingStartDateComparator implements Comparator<Booking> {
    @Override
    public int compare(Booking o1, Booking o2) {
        if (o1.getStartDate().isAfter(o2.getStartDate())) return -1;
        else if (o1.getStartDate().isBefore(o2.getStartDate())) return 1;
        else return 0;
    }
}
