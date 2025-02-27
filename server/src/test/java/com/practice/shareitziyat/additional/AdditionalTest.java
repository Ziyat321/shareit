package com.practice.shareitziyat.additional;

import com.practice.shareitziyat.server.booking.Booking;
import com.practice.shareitziyat.server.comparators.BookingStartDateComparator;
import com.practice.shareitziyat.server.comparators.RequestCreatedDateComparator;
import com.practice.shareitziyat.server.exceptions.*;
import com.practice.shareitziyat.server.request.Request;
import com.practice.shareitziyat.server.utils.RequestConstants;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class AdditionalTest {
    private final ErrorHandler errorHandler = new ErrorHandler();
    private final RequestCreatedDateComparator requestCreatedDateComparator= new RequestCreatedDateComparator();
    private final BookingStartDateComparator bookingStartDateComparator = new BookingStartDateComparator();
    private final RequestConstants requestConstants = new RequestConstants();

    @Test
    public void handleWrongOwnerTest() {
        ForbiddenException exception = new ForbiddenException("msg");

        ErrorResponse errorResponse = errorHandler.handleWrongOwner(exception);

        assertEquals("Wrong owner", errorResponse.getError());
        assertEquals("msg", errorResponse.getDescription());
    }

    @Test
    public void handleNotFoundTest() {
        NotFoundException exception = new NotFoundException("msg");

        ErrorResponse errorResponse = errorHandler.handleNotFound(exception);

        assertEquals("User not found", errorResponse.getError());
        assertEquals("msg", errorResponse.getDescription());
    }

    @Test
    public void handleBadRequestTest() {
        BadRequestException exception = new BadRequestException("msg");

        ErrorResponse errorResponse = errorHandler.handleBadRequest(exception);

        assertEquals("Bad Request", errorResponse.getError());
        assertEquals("msg", errorResponse.getDescription());
    }

    @Test
    public void requestCreatedDateComparatorTest1() {
        Request request1 = new Request();
        request1.setCreated(LocalDateTime.of(2025, 2, 16, 12, 0, 0));
        Request request2 = new Request();
        request2.setCreated(LocalDateTime.of(2025, 2, 17, 12, 0, 0));

        int result = requestCreatedDateComparator.compare(request1, request2);

        assertEquals(1, result);
    }

    @Test
    public void requestCreatedDateComparatorTest2() {
        Request request1 = new Request();
        request1.setCreated(LocalDateTime.of(2025, 2, 16, 12, 0, 0));
        Request request2 = new Request();
        request2.setCreated(LocalDateTime.of(2025, 2, 17, 12, 0, 0));

        int result = requestCreatedDateComparator.compare(request2, request1);

        assertEquals(-1, result);
    }

    @Test
    public void requestCreatedDateComparatorTest3() {
        Request request1 = new Request();
        request1.setCreated(LocalDateTime.of(2025, 2, 17, 12, 0, 0));
        Request request2 = new Request();
        request2.setCreated(LocalDateTime.of(2025, 2, 17, 12, 0, 0));

        int result = requestCreatedDateComparator.compare(request1, request2);

        assertEquals(0, result);
    }

    @Test
    public void bookingStartDateComparatorTest1() {
        Booking booking1 = new Booking();
        booking1.setStartDate(LocalDateTime.of(2025, 2, 16, 12, 0, 0));
        Booking booking2 = new Booking();
        booking2.setStartDate(LocalDateTime.of(2025, 2, 17, 12, 0, 0));

        int result = bookingStartDateComparator.compare(booking1, booking2);

        assertEquals(1, result);
    }

    @Test
    public void bookingStartDateComparatorTest2() {
        Booking booking1 = new Booking();
        booking1.setStartDate(LocalDateTime.of(2025, 2, 16, 12, 0, 0));
        Booking booking2 = new Booking();
        booking2.setStartDate(LocalDateTime.of(2025, 2, 17, 12, 0, 0));

        int result = bookingStartDateComparator.compare(booking2, booking1);

        assertEquals(-1, result);
    }

    @Test
    public void bookingStartDateComparatorTest3() {
        Booking booking1 = new Booking();
        booking1.setStartDate(LocalDateTime.of(2025, 2, 17, 12, 0, 0));
        Booking booking2 = new Booking();
        booking2.setStartDate(LocalDateTime.of(2025, 2, 17, 12, 0, 0));

        int result = bookingStartDateComparator.compare(booking1, booking2);

        assertEquals(0, result);
    }

    @Test
    public void utilCheck() {
        String value = requestConstants.USER_HEADER;

        assertEquals("X-Sharer-User-Id", value);
    }
}
