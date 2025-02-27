package com.practice.shareitziyat.server.booking;

import com.practice.shareitziyat.server.item.Item;
import com.practice.shareitziyat.server.item.ItemRepository;
import com.practice.shareitziyat.server.user.User;
import com.practice.shareitziyat.server.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Rollback(value = false)
public class BookingRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BookingRepository bookingRepository;

    private List<Booking> bookings = new ArrayList<>();

    @BeforeEach
    public void init() {
        User user1 = new User();
        user1.setName("user1");
        user1.setEmail("user1@email.com");
        User savedUser1 = userRepository.save(user1);
        User user2 = new User();
        user2.setName("user2");
        user2.setEmail("user2@email.com");
        User savedUser2 = userRepository.save(user2);
        User owner = new User();
        owner.setName("owner");
        owner.setEmail("owner@email.com");
        User savedOwner = userRepository.save(owner);
        Item item1 = new Item();
        item1.setName("item1");
        item1.setDescription("description1");
        item1.setAvailable(true);
        item1.setOwner(savedOwner);
        Item savedItem1 = itemRepository.save(item1);
        Item item2 = new Item();
        item2.setName("item2");
        item2.setDescription("description2");
        item2.setAvailable(true);
        item2.setOwner(savedOwner);
        Item savedItem2 = itemRepository.save(item2);
        Item item3 = new Item();
        item3.setName("item3");
        item3.setDescription("description3");
        item3.setAvailable(true);
        item3.setOwner(savedOwner);
        Item savedItem3 = itemRepository.save(item3);
        Booking booking1 = new Booking();
        booking1.setStartDate(LocalDateTime.of(2025, 2,8, 12, 0, 0));
        booking1.setEndDate(LocalDateTime.of(2025, 12, 31, 12, 0, 0));
        booking1.setStatus(BookingStatus.WAITING);
        booking1.setItem(savedItem1);
        booking1.setUser(savedUser1);
        bookingRepository.save(booking1);
        Booking booking2 = new Booking();
        booking2.setStartDate(LocalDateTime.of(2025, 2,9, 12, 0, 0));
        booking2.setEndDate(LocalDateTime.of(2025, 12, 31, 12, 0, 0));
        booking2.setStatus(BookingStatus.APPROVED);
        booking2.setItem(savedItem2);
        booking2.setUser(savedUser1);
        bookingRepository.save(booking2);
        Booking booking3 = new Booking();
        booking3.setStartDate(LocalDateTime.of(2025, 2,10, 12, 0, 0));
        booking3.setEndDate(LocalDateTime.of(2025, 12, 31, 12, 0, 0));
        booking3.setStatus(BookingStatus.WAITING);
        booking3.setItem(savedItem3);
        booking3.setUser(savedUser2);
        bookingRepository.save(booking3);
    }

    @Test
    public void findAllByUser_IdOrderByStartDateDescTest() {
        bookings = bookingRepository.findAllByUser_IdOrderByStartDateDesc(1L);
        check();
    }

    @Test
    public void findAllByUser_IdAndStartDateBeforeOrderByStartDateDescTest() {
        bookings = bookingRepository.findAllByUser_IdAndStartDateBeforeOrderByStartDateDesc(1L,
                LocalDateTime.of(2025, 2, 10, 12, 0, 0));
        check();
    }

    @Test
    public void findAllByUser_IdAndEndDateAfterOrderByStartDateDescTest() {
        bookings = bookingRepository.findAllByUser_IdAndEndDateAfterOrderByStartDateDesc(1L,
                LocalDateTime.of(2025, 11, 1, 12, 0, 0));
        check();
    }

    @Test
    public void findAllByUser_IdAndStartDateBeforeAndEndDateAfterOrderByStartDateDescTest() {
        bookings = bookingRepository.findAllByUser_IdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(1L,
                LocalDateTime.of(2025, 2, 10, 12, 0, 0),
                LocalDateTime.of(2025, 11, 1, 12, 0, 0));
        check();
    }

    @Test
    public void findAllByUser_IdAndStatusIsTest() {
        bookings = bookingRepository.findAllByUser_IdAndStatusIs(1L, BookingStatus.WAITING);

        assertEquals(1, bookings.size());
        assertEquals(LocalDateTime.of(2025, 2,8, 12, 0, 0),
                bookings.get(0).getStartDate());
        assertEquals(LocalDateTime.of(2025, 12, 31, 12, 0, 0),
                bookings.get(0).getEndDate());
        assertEquals(BookingStatus.WAITING, bookings.get(0).getStatus());
        assertEquals("item1", bookings.get(0).getItem().getName());
        assertEquals("description1", bookings.get(0).getItem().getDescription());
        assertEquals(true, bookings.get(0).getItem().getAvailable());
        assertEquals("owner", bookings.get(0).getItem().getOwner().getName());
        assertEquals("owner@email.com", bookings.get(0).getItem().getOwner().getEmail());
        assertEquals("user1", bookings.get(0).getUser().getName());
        assertEquals("user1@email.com", bookings.get(0).getUser().getEmail());
    }

    @Test
    public void findAllByItem_Owner_IdOrderByStartDateDescTest() {
        bookings = bookingRepository.findAllByItem_Owner_IdOrderByStartDateDesc(3L);

        check1();
    }

    @Test
    public void findAllByItem_Owner_IdAndStartDateBeforeOrderByStartDateDesc() {
        bookings = bookingRepository.findAllByItem_Owner_IdAndStartDateBeforeOrderByStartDateDesc(3L,
                LocalDateTime.of(2025, 2, 20, 12, 0, 0));

        check1();
    }

    @Test
    public void findAllByItem_Owner_IdAndEndDateAfterOrderByStartDateDesc() {
        bookings = bookingRepository.findAllByItem_Owner_IdAndEndDateAfterOrderByStartDateDesc(3L,
                LocalDateTime.of(2025, 11, 10, 12, 0, 0));

        check1();
    }

    @Test
    public void findAllByItem_Owner_IdAndStartDateBeforeAndEndDateAfterOrderByStartDateDescTest() {
        bookings = bookingRepository.findAllByItem_Owner_IdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(
                3L,LocalDateTime.of(2025, 2, 20, 12, 0, 0),
                LocalDateTime.of(2025, 11, 10, 12, 0, 0)
                );

        check1();
    }

    @Test
    public void findAllByItem_Owner_IdAndStatusIsTest() {
        bookings = bookingRepository.findAllByItem_Owner_IdAndStatusIs(3L, BookingStatus.WAITING);

        assertEquals(2, bookings.size());
        assertEquals(LocalDateTime.of(2025, 2,8, 12, 0, 0),
                bookings.get(0).getStartDate());
        assertEquals(LocalDateTime.of(2025, 12, 31, 12, 0, 0),
                bookings.get(0).getEndDate());
        assertEquals(BookingStatus.WAITING, bookings.get(0).getStatus());
        assertEquals("item1", bookings.get(0).getItem().getName());
        assertEquals("description1", bookings.get(0).getItem().getDescription());
        assertEquals(true, bookings.get(0).getItem().getAvailable());
        assertEquals("owner", bookings.get(0).getItem().getOwner().getName());
        assertEquals("owner@email.com", bookings.get(0).getItem().getOwner().getEmail());
        assertEquals("user1", bookings.get(0).getUser().getName());
        assertEquals("user1@email.com", bookings.get(0).getUser().getEmail());
        assertEquals(LocalDateTime.of(2025, 2,10, 12, 0, 0),
                bookings.get(1).getStartDate());
        assertEquals(LocalDateTime.of(2025, 12, 31, 12, 0, 0),
                bookings.get(1).getEndDate());
        assertEquals(BookingStatus.WAITING, bookings.get(1).getStatus());
        assertEquals("item3", bookings.get(1).getItem().getName());
        assertEquals("description3", bookings.get(1).getItem().getDescription());
        assertEquals(true, bookings.get(1).getItem().getAvailable());
        assertEquals("owner", bookings.get(1).getItem().getOwner().getName());
        assertEquals("owner@email.com", bookings.get(1).getItem().getOwner().getEmail());
        assertEquals("user2", bookings.get(1).getUser().getName());
        assertEquals("user2@email.com", bookings.get(1).getUser().getEmail());
    }

    @Test
    public void findByUser_IdAndItem_IdAndStatusIsAndStartDateBeforeTest() {
        bookings = bookingRepository.findByUser_IdAndItem_IdAndStatusIsAndStartDateBefore(1L,1L,
                BookingStatus.WAITING, LocalDateTime.of(2025, 2, 15, 12, 0, 0));

        assertEquals(1, bookings.size());
        assertEquals(LocalDateTime.of(2025, 2,8, 12, 0, 0),
                bookings.get(0).getStartDate());
        assertEquals(LocalDateTime.of(2025, 12, 31, 12, 0, 0),
                bookings.get(0).getEndDate());
        assertEquals(BookingStatus.WAITING, bookings.get(0).getStatus());
        assertEquals("item1", bookings.get(0).getItem().getName());
        assertEquals("description1", bookings.get(0).getItem().getDescription());
        assertEquals(true, bookings.get(0).getItem().getAvailable());
        assertEquals("owner", bookings.get(0).getItem().getOwner().getName());
        assertEquals("owner@email.com", bookings.get(0).getItem().getOwner().getEmail());
        assertEquals("user1", bookings.get(0).getUser().getName());
        assertEquals("user1@email.com", bookings.get(0).getUser().getEmail());
    }

    private void check() {
        assertEquals(2, bookings.size());
        assertEquals(LocalDateTime.of(2025, 2,9, 12, 0, 0),
                bookings.get(0).getStartDate());
        assertEquals(LocalDateTime.of(2025, 12, 31, 12, 0, 0),
                bookings.get(0).getEndDate());
        assertEquals(BookingStatus.APPROVED, bookings.get(0).getStatus());
        assertEquals("item2", bookings.get(0).getItem().getName());
        assertEquals("description2", bookings.get(0).getItem().getDescription());
        assertEquals(true, bookings.get(0).getItem().getAvailable());
        assertEquals("owner", bookings.get(0).getItem().getOwner().getName());
        assertEquals("owner@email.com", bookings.get(0).getItem().getOwner().getEmail());
        assertEquals("user1", bookings.get(0).getUser().getName());
        assertEquals("user1@email.com", bookings.get(0).getUser().getEmail());
        assertEquals(LocalDateTime.of(2025, 2,8, 12, 0, 0),
                bookings.get(1).getStartDate());
        assertEquals(LocalDateTime.of(2025, 12, 31, 12, 0, 0),
                bookings.get(1).getEndDate());
        assertEquals(BookingStatus.WAITING, bookings.get(1).getStatus());
        assertEquals("item1", bookings.get(1).getItem().getName());
        assertEquals("description1", bookings.get(1).getItem().getDescription());
        assertEquals(true, bookings.get(1).getItem().getAvailable());
        assertEquals("owner", bookings.get(1).getItem().getOwner().getName());
        assertEquals("owner@email.com", bookings.get(1).getItem().getOwner().getEmail());
        assertEquals("user1", bookings.get(1).getUser().getName());
        assertEquals("user1@email.com", bookings.get(1).getUser().getEmail());
    }

    private void check1() {
        assertEquals(3, bookings.size());
        assertEquals(LocalDateTime.of(2025, 2,10, 12, 0, 0),
                bookings.get(0).getStartDate());
        assertEquals(LocalDateTime.of(2025, 12, 31, 12, 0, 0),
                bookings.get(0).getEndDate());
        assertEquals(BookingStatus.WAITING, bookings.get(0).getStatus());
        assertEquals("item3", bookings.get(0).getItem().getName());
        assertEquals("description3", bookings.get(0).getItem().getDescription());
        assertEquals(true, bookings.get(0).getItem().getAvailable());
        assertEquals("owner", bookings.get(0).getItem().getOwner().getName());
        assertEquals("owner@email.com", bookings.get(0).getItem().getOwner().getEmail());
        assertEquals("user2", bookings.get(0).getUser().getName());
        assertEquals("user2@email.com", bookings.get(0).getUser().getEmail());
        assertEquals(LocalDateTime.of(2025, 2,9, 12, 0, 0),
                bookings.get(1).getStartDate());
        assertEquals(LocalDateTime.of(2025, 12, 31, 12, 0, 0),
                bookings.get(1).getEndDate());
        assertEquals(BookingStatus.APPROVED, bookings.get(1).getStatus());
        assertEquals("item2", bookings.get(1).getItem().getName());
        assertEquals("description2", bookings.get(1).getItem().getDescription());
        assertEquals(true, bookings.get(1).getItem().getAvailable());
        assertEquals("owner", bookings.get(1).getItem().getOwner().getName());
        assertEquals("owner@email.com", bookings.get(1).getItem().getOwner().getEmail());
        assertEquals("user1", bookings.get(1).getUser().getName());
        assertEquals("user1@email.com", bookings.get(1).getUser().getEmail());
        assertEquals(LocalDateTime.of(2025, 2,8, 12, 0, 0),
                bookings.get(2).getStartDate());
        assertEquals(LocalDateTime.of(2025, 12, 31, 12, 0, 0),
                bookings.get(2).getEndDate());
        assertEquals(BookingStatus.WAITING, bookings.get(2).getStatus());
        assertEquals("item1", bookings.get(2).getItem().getName());
        assertEquals("description1", bookings.get(2).getItem().getDescription());
        assertEquals(true, bookings.get(2).getItem().getAvailable());
        assertEquals("owner", bookings.get(2).getItem().getOwner().getName());
        assertEquals("owner@email.com", bookings.get(2).getItem().getOwner().getEmail());
        assertEquals("user1", bookings.get(2).getUser().getName());
        assertEquals("user1@email.com", bookings.get(2).getUser().getEmail());
    }
}
