package com.practice.shareitziyat.server.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findAllByUser_IdOrderByStartDateDesc(Long userId, Pageable pageable);

    Page<Booking> findAllByUser_IdAndEndDateBeforeOrderByStartDateDesc(Long userId, LocalDateTime date, Pageable pageable);

    Page<Booking> findAllByUser_IdAndEndDateAfterOrderByStartDateDesc(Long userId, LocalDateTime date, Pageable pageable);

    Page<Booking> findAllByUser_IdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(Long userId, LocalDateTime dateStart,
                                                                                        LocalDateTime dateEnd, Pageable pageable);

    Page<Booking> findAllByUser_IdAndStatusIsOrderByStartDateDesc(Long userId, BookingStatus status, Pageable pageable);

    Page<Booking> findAllByItem_Owner_IdOrderByStartDateDesc(Long ownerId, Pageable pageable);

    Page<Booking> findAllByItem_Owner_IdAndEndDateBeforeOrderByStartDateDesc(Long userId, LocalDateTime date, Pageable pageable);

    Page<Booking> findAllByItem_Owner_IdAndEndDateAfterOrderByStartDateDesc(Long userId, LocalDateTime date, Pageable pageable);

    Page<Booking> findAllByItem_Owner_IdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(Long userId, LocalDateTime dateStart,
                                                                                              LocalDateTime dateEnd, Pageable pageable);

    Page<Booking> findAllByItem_Owner_IdAndStatusIsOrderByStartDateDesc(Long userId, BookingStatus status, Pageable pageable);

    List<Booking> findByUser_IdAndItem_IdAndStatusIsAndStartDateBefore(Long userId,Long itemId, BookingStatus status, LocalDateTime date);

    @Query("select b from Booking b " +
            "where b.item.id = :itemId " +
            "and b.startDate < :time " +
            "and b.status = :status " +
            "order by b.startDate desc limit 1")
    Optional<Booking> findLastBooking(Long itemId, LocalDateTime time, BookingStatus status);

    @Query("select b from Booking b " +
            "where b.item.id = :itemId " +
            "and b.startDate > :time " +
            "and b.status = :status " +
            "order by b.startDate limit 1")
    Optional<Booking> findNextBooking(Long itemId, LocalDateTime time, BookingStatus status);
}
