package com.practice.shareitziyat.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByUser_IdOrderByStartDateDesc(Long userId);

    List<Booking> findAllByUser_IdAndStartDateBeforeOrderByStartDateDesc(Long userId, LocalDateTime date);

    List<Booking> findAllByUser_IdAndEndDateAfterOrderByStartDateDesc(Long userId, LocalDateTime date);

    List<Booking> findAllByUser_IdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(Long userId, LocalDateTime dateStart,
                                                                                        LocalDateTime dateEnd);

    List<Booking> findAllByUser_IdAndStatusIs(Long userId, BookingStatus status);

    List<Booking> findAllByItem_Owner_IdOrderByStartDateDesc(Long ownerId);

    List<Booking> findAllByItem_Owner_IdAndStartDateBeforeOrderByStartDateDesc(Long userId, LocalDateTime date);

    List<Booking> findAllByItem_Owner_IdAndEndDateAfterOrderByStartDateDesc(Long userId, LocalDateTime date);

    List<Booking> findAllByItem_Owner_IdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(Long userId, LocalDateTime dateStart,
                                                                                              LocalDateTime dateEnd);

    List<Booking> findAllByItem_Owner_IdAndStatusIs(Long userId, BookingStatus status);

    List<Booking> findByUser_IdAndItem_IdAndStatusIsAndStartDateBefore(Long userId,Long itemId, BookingStatus status, LocalDateTime date);

}
