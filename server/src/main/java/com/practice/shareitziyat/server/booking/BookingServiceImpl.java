package com.practice.shareitziyat.server.booking;

import com.practice.shareitziyat.server.exceptions.BadRequestException;
import com.practice.shareitziyat.server.exceptions.NotFoundException;
import com.practice.shareitziyat.server.exceptions.ForbiddenException;
import com.practice.shareitziyat.server.item.Item;
import com.practice.shareitziyat.server.item.ItemRepository;
import com.practice.shareitziyat.server.user.User;
import com.practice.shareitziyat.server.user.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public Booking create(Booking booking, Long userId) {
        if (!booking.getStartDate().isBefore(booking.getEndDate()))
        // TODO add msg exception
        {
            throw new BadRequestException("");
        }

        User owner = getUserById(userId);

        Item item = itemRepository.findById(booking.getItem().getId()).orElseThrow(() -> new NotFoundException("Item not found"));
        if (!item.getAvailable()) {
            throw new BadRequestException("Item is not available");
        }
        if (item.getOwner().getId().equals(owner.getId())) {
            throw new ForbiddenException("Wrong owner");
        }
        booking.setUser(owner);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking update(long bookingId, Long userId, boolean approved) {
        Booking bookingExisting = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException(
                "Booking does not exist"));

        if (!bookingExisting.getItem().getOwner().getId().equals(userId)) {
            throw new NotFoundException("Wrong owner");
        }

        // если booking.approved и approved=true - 400 BAD REQUEST
        if(bookingExisting.getStatus().equals(BookingStatus.APPROVED) && approved) {
            throw new BadRequestException("Approved booking cannot be approved once again");
        }

        BookingStatus bookingStatus = approved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        bookingExisting.setStatus(bookingStatus);
        return bookingRepository.save(bookingExisting);

    }

    @Override
    public Booking findById(long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Booking not found"));
        if (!booking.getUser().getId().equals(userId)
                && !booking.getItem().getOwner().getId().equals(userId)) {
            // TODO: add msg
            throw new NotFoundException("");
        }
        return booking;
    }

    @Override
    public List<Booking> findAllByOwner(Long userId, BookingState state, int page, int size) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new ForbiddenException("Wrong user"));
        Pageable pageable = PageRequest.of(page, size);
        Page<Booking> pageResult = switch (state) {
            case ALL -> bookingRepository.findAllByItem_Owner_IdOrderByStartDateDesc(owner.getId(), pageable);
            case PAST ->
                    bookingRepository.findAllByItem_Owner_IdAndEndDateBeforeOrderByStartDateDesc(owner.getId(), LocalDateTime.now(), pageable);
            case FUTURE ->
                    bookingRepository.findAllByItem_Owner_IdAndEndDateAfterOrderByStartDateDesc(owner.getId(), LocalDateTime.now(), pageable);
            case CURRENT ->
                    bookingRepository.findAllByItem_Owner_IdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(
                            owner.getId(), LocalDateTime.now(), LocalDateTime.now(), pageable
                    );
            case WAITING ->
                    bookingRepository.findAllByItem_Owner_IdAndStatusIsOrderByStartDateDesc(owner.getId(), BookingStatus.WAITING, pageable);
            case REJECTED ->
                    bookingRepository.findAllByItem_Owner_IdAndStatusIsOrderByStartDateDesc(owner.getId(), BookingStatus.REJECTED, pageable);
        };
        return pageResult.getContent();
    }

    @Override
    public List<Booking> findAllByBooker(Long bookerId, BookingState state, int page, int size) {
        User booker = getUserById(bookerId);
        Pageable pageable = PageRequest.of(page, size);
        Page<Booking> pageResult = switch (state) {
            case ALL -> bookingRepository.findAllByUser_IdOrderByStartDateDesc(booker.getId(), pageable);
            case PAST -> bookingRepository.findAllByUser_IdAndEndDateBeforeOrderByStartDateDesc(booker.getId(), LocalDateTime.now(), pageable);
            case FUTURE -> bookingRepository.findAllByUser_IdAndEndDateAfterOrderByStartDateDesc(booker.getId(), LocalDateTime.now(), pageable);
            case CURRENT ->  bookingRepository.findAllByUser_IdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(
                        booker.getId(), LocalDateTime.now(), LocalDateTime.now(), pageable);
            case WAITING -> bookingRepository.findAllByUser_IdAndStatusIsOrderByStartDateDesc(booker.getId(), BookingStatus.WAITING, pageable);
            case REJECTED -> bookingRepository.findAllByUser_IdAndStatusIsOrderByStartDateDesc(booker.getId(), BookingStatus.REJECTED, pageable);
        };
        return pageResult.getContent();
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
    }
}
