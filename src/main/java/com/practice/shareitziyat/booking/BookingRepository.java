package com.practice.shareitziyat.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByUser_IdOrderByStartDateDesc(int userId);

    List<Booking> findAllByUser_IdAndStartDateBeforeOrderByStartDateDesc(int userId, LocalDateTime date);

    List<Booking> findAllByUser_IdAndEndDateAfterOrderByStartDateDesc(int userId, LocalDateTime date);

    List<Booking> findAllByUser_IdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(int userId, LocalDateTime dateStart,
                                                                                        LocalDateTime dateEnd);

    List<Booking> findAllByUser_IdAndStatusIs(int userId, BookingStatus status);

    List<Booking> findAllByItem_Owner_IdOrderByStartDateDesc(int ownerId);

    List<Booking> findAllByItem_Owner_IdAndStartDateBeforeOrderByStartDateDesc(int userId, LocalDateTime date);

    List<Booking> findAllByItem_Owner_IdAndEndDateAfterOrderByStartDateDesc(int userId, LocalDateTime date);

    List<Booking> findAllByItem_Owner_IdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(int userId, LocalDateTime dateStart,
                                                                                              LocalDateTime dateEnd);

    List<Booking> findAllByItem_Owner_IdAndStatusIs(int userId, BookingStatus status);

}
