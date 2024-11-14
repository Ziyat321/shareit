package com.practice.shareitziyat.booking.dto;

import com.practice.shareitziyat.booking.Booking;
import com.practice.shareitziyat.exceptions.NotFoundException;
import com.practice.shareitziyat.item.Item;
import com.practice.shareitziyat.item.ItemRepository;
import com.practice.shareitziyat.item.dto.ItemMapper;
import com.practice.shareitziyat.user.dto.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingMapper {

    private final ItemMapper itemMapper;
    private final UserMapper userMapper;

    public Booking fromCreate(BookingCreateDto bookingCreate){
        Booking booking = new Booking();
        booking.setStartDate(bookingCreate.getStart());
        booking.setEndDate(bookingCreate.getEnd());
        Item item = new Item();
        item.setId(bookingCreate.getItemId());
        booking.setItem(item);
        return booking;
    }

    public BookingResponseDto toResponse(Booking booking){
        BookingResponseDto bookingResponse = new BookingResponseDto();
        bookingResponse.setId(booking.getId());
        bookingResponse.setStart(booking.getStartDate());
        bookingResponse.setEnd(booking.getEndDate());
        bookingResponse.setStatus(booking.getStatus());
        bookingResponse.setItem(itemMapper.toResponse(booking.getItem()));
        bookingResponse.setBooker(userMapper.toResponse(booking.getUser()));
        return bookingResponse;
    }

    public List<BookingResponseDto> toResponse(List<Booking> bookings){
        return bookings.stream().map(this::toResponse).toList();
    }
}
