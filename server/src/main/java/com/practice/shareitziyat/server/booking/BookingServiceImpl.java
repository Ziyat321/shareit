package com.practice.shareitziyat.server.booking;

import com.practice.shareitziyat.server.exceptions.BadRequestException;
import com.practice.shareitziyat.server.exceptions.NotFoundException;
import com.practice.shareitziyat.server.exceptions.ForbiddenException;
import com.practice.shareitziyat.server.item.Item;
import com.practice.shareitziyat.server.item.ItemRepository;
import com.practice.shareitziyat.server.user.User;
import com.practice.shareitziyat.server.user.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{
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

        Item item = itemRepository.findById(booking.getItem().getId()).orElseThrow(()-> new NotFoundException("Item not found"));
        if(!item.getAvailable()) {
            throw new BadRequestException("Item is not available");
        }
        if(item.getOwner().getId().equals(owner.getId())) {
            throw new ForbiddenException("Wrong owner");
        }
        booking.setUser(owner);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking update(long bookingId, Long userId, boolean approved) {
        Booking bookingExisting = bookingRepository.findById(bookingId).orElseThrow(()-> new NotFoundException(
                "Booking does not exist"));

        if(!bookingExisting.getItem().getOwner().getId().equals(userId)) {
            throw new NotFoundException("Wrong owner");
        }

        // если booking.approved и approved=true - 400 BAD REQUEST

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
    public List<Booking> findAllByOwner(Long userId, BookingState state) {
        User owner = userRepository.findById(userId).orElseThrow(()->new ForbiddenException("Wrong user"));
        switch (state){
            case ALL -> {
                return bookingRepository.findAllByItem_Owner_IdOrderByStartDateDesc(owner.getId());
            }
            case PAST -> {
                return bookingRepository.findAllByItem_Owner_IdAndStartDateBeforeOrderByStartDateDesc(owner.getId(), LocalDateTime.now());
            }
            case FUTURE -> {
                return bookingRepository.findAllByItem_Owner_IdAndEndDateAfterOrderByStartDateDesc(owner.getId(), LocalDateTime.now());
            }
            case CURRENT -> {
                return bookingRepository.findAllByItem_Owner_IdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(
                        owner.getId(), LocalDateTime.now(), LocalDateTime.now()
                );
            }
            case WAITING -> {
                return bookingRepository.findAllByItem_Owner_IdAndStatusIsOrderByStartDateDesc(owner.getId(), BookingStatus.WAITING);
            }
            case REJECTED -> {
                return bookingRepository.findAllByItem_Owner_IdAndStatusIsOrderByStartDateDesc(owner.getId(), BookingStatus.REJECTED);
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    @Override
    public List<Booking> findAllByBooker(Long bookerId, BookingState state) {
        User booker = getUserById(bookerId);
        switch (state){
            case ALL -> {
                return bookingRepository.findAllByUser_IdOrderByStartDateDesc(booker.getId());
            }
            case PAST -> {
                return bookingRepository.findAllByUser_IdAndStartDateBeforeOrderByStartDateDesc(booker.getId(), LocalDateTime.now());
            }
            case FUTURE -> {
                return bookingRepository.findAllByUser_IdAndEndDateAfterOrderByStartDateDesc(booker.getId(), LocalDateTime.now());
            }
            case CURRENT -> {
                return bookingRepository.findAllByUser_IdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(
                        booker.getId(), LocalDateTime.now(), LocalDateTime.now());
            }
            case WAITING -> {
                return bookingRepository.findAllByUser_IdAndStatusIsOrderByStartDateDesc(booker.getId(), BookingStatus.WAITING);
            }
            case REJECTED -> {
                return bookingRepository.findAllByUser_IdAndStatusIsOrderByStartDateDesc(booker.getId(), BookingStatus.REJECTED);
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(()-> new NotFoundException("User not found"));
    }
}
