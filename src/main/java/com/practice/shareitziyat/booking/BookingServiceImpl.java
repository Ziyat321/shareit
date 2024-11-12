package com.practice.shareitziyat.booking;

import com.practice.shareitziyat.exceptions.NotFoundException;
import com.practice.shareitziyat.exceptions.WrongOwnerException;
import com.practice.shareitziyat.user.User;
import com.practice.shareitziyat.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    @Override
    public Booking create(Booking booking, int userId) {
        User owner = userRepository.findById(userId).orElseThrow(()-> new NotFoundException("User not found"));
        booking.setUser(owner);
        booking.setStatus(BookingStatus.WAITING);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking update(Booking booking, long bookingId, int userId, boolean approved) {
        User owner = userRepository.findById(userId).orElseThrow(()-> new NotFoundException("User not found"));
        BookingStatus bookingStatus = approved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        Booking bookingExisting = bookingRepository.findById(bookingId).orElseThrow(()-> new NotFoundException(
                "Booking does not exist"));
        User ownerExisting = bookingExisting.getUser();
        if(!ownerExisting.equals(owner)) throw new WrongOwnerException("Wrong owner");
        bookingExisting.setStatus(bookingStatus);
        return bookingRepository.save(bookingExisting);

    }

    @Override
    public Booking findById(long bookingId, int userId) {
        User owner = userRepository.findById(userId).orElseThrow(()-> new NotFoundException("User not found"));
        return bookingRepository.findById(bookingId).orElseThrow(()->new NotFoundException("Booking not found"));
    }

    @Override
    public List<Booking> findAllByOwner(int userId, BookingState state) {
        User owner = userRepository.findById(userId).orElseThrow(()-> new NotFoundException("User not found"));
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
                return bookingRepository.findAllByItem_Owner_IdAndStatusIs(owner.getId(), BookingStatus.WAITING);
            }
            case REJECTED -> {
                return bookingRepository.findAllByItem_Owner_IdAndStatusIs(owner.getId(), BookingStatus.REJECTED);
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    @Override
    public List<Booking> findAllByBooker(int bookerId, BookingState state) {
        User booker = userRepository.findById(bookerId).orElseThrow(()-> new NotFoundException("User not found"));
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
                return bookingRepository.findAllByUser_IdAndStatusIs(booker.getId(), BookingStatus.WAITING);
            }
            case REJECTED -> {
                return bookingRepository.findAllByUser_IdAndStatusIs(booker.getId(), BookingStatus.REJECTED);
            }
            default -> {
                return Collections.emptyList();
            }
        }


    }
}
