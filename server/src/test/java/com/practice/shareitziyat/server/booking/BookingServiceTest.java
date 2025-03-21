package com.practice.shareitziyat.server.booking;

import com.practice.shareitziyat.server.booking.dto.BookingMapper;
import com.practice.shareitziyat.server.comparators.BookingStartDateComparator;
import com.practice.shareitziyat.server.exceptions.BadRequestException;
import com.practice.shareitziyat.server.exceptions.ForbiddenException;
import com.practice.shareitziyat.server.exceptions.NotFoundException;
import com.practice.shareitziyat.server.item.Item;
import com.practice.shareitziyat.server.item.ItemRepository;
import com.practice.shareitziyat.server.item.dto.ItemMapper;
import com.practice.shareitziyat.server.user.User;
import com.practice.shareitziyat.server.user.UserRepository;
import com.practice.shareitziyat.server.user.dto.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @Mock
    BookingRepository bookingRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ItemRepository itemRepository;

    @Spy
    ItemMapper itemMapper;

    @Spy
    UserMapper userMapper;

    @Spy
    BookingMapper bookingMapper = new BookingMapper(itemMapper, userMapper);

    private List<User> bookers = new ArrayList<>();

    private List<User> owners = new ArrayList<>();

    private List<Booking> bookings = new ArrayList<>();

    private List<Booking> bookingList = new ArrayList<>();

    final int CURRENT_YEAR = LocalDateTime.now().getYear();

    final int NEXT_YEAR = CURRENT_YEAR + 1;

    @Test
    public void createTest() {
        BookingService bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);
        Item bookedItem = new Item();
        bookedItem.setId(1L);
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStartDate(LocalDateTime.of(2025, 2, 13, 12, 0, 0));
        booking.setEndDate(LocalDateTime.of(2025, 12, 31, 12, 0, 0));
        booking.setItem(bookedItem);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    long userId = invocationOnMock.getArgument(0);
                    User booker = new User();
                    booker.setId(userId);
                    booker.setName("booker");
                    booker.setEmail("booker@email.com");
                    return Optional.of(booker);
                });
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    long itemId = invocationOnMock.getArgument(0);
                    Item item = new Item();
                    item.setId(itemId);
                    item.setName("item");
                    item.setDescription("description");
                    item.setAvailable(true);
                    User owner = new User();
                    owner.setId(1L);
                    owner.setName("item_owner");
                    owner.setEmail("item_owner@email.com");
                    item.setOwner(owner);
                    return Optional.of(item);
                });
        Mockito.when(bookingRepository.save(Mockito.any(Booking.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        Booking createdBooking = bookingService.create(booking, 2L);

        assertEquals(1, createdBooking.getId());
        assertEquals(LocalDateTime.of(2025, 2, 13, 12, 0, 0),
                createdBooking.getStartDate());
        assertEquals(LocalDateTime.of(2025, 12, 31, 12, 0, 0),
                createdBooking.getEndDate());
        assertEquals(BookingStatus.WAITING, booking.getStatus());
        assertEquals(2, createdBooking.getUser().getId());
        assertEquals("booker", createdBooking.getUser().getName());
        assertEquals("booker@email.com", createdBooking.getUser().getEmail());
        assertEquals(1, createdBooking.getItem().getId());
        assertEquals("item", createdBooking.getItem().getName());
        assertEquals("description", createdBooking.getItem().getDescription());
        assertEquals(true, createdBooking.getItem().getAvailable());
        assertEquals(1, createdBooking.getItem().getOwner().getId());
        assertEquals("item_owner", createdBooking.getItem().getOwner().getName());
        assertEquals("item_owner@email.com", createdBooking.getItem().getOwner().getEmail());
    }

    @Test
    public void createExceptionTest1() {
        BookingService bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);
        Item bookedItem = new Item();
        bookedItem.setId(1L);
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStartDate(LocalDateTime.of(2025, 2, 13, 12, 0, 0));
        booking.setEndDate(LocalDateTime.of(2025, 12, 31, 12, 0, 0));
        booking.setItem(bookedItem);

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.create(booking, 2L));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void createExceptionTest2() {
        BookingService bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);
        Item bookedItem = new Item();
        bookedItem.setId(1L);
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStartDate(LocalDateTime.of(2025, 2, 13, 12, 0, 0));
        booking.setEndDate(LocalDateTime.of(2025, 12, 31, 12, 0, 0));
        booking.setItem(bookedItem);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    long userId = invocationOnMock.getArgument(0);
                    User booker = new User();
                    booker.setId(userId);
                    booker.setName("booker");
                    booker.setEmail("booker@email.com");
                    return Optional.of(booker);
                });
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.create(booking, 2L));

        assertEquals("Item not found", exception.getMessage());
    }

    @Test
    public void createExceptionTest3() {
        BookingService bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);
        Item bookedItem = new Item();
        bookedItem.setId(1L);
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStartDate(LocalDateTime.of(2025, 2, 13, 12, 0, 0));
        booking.setEndDate(LocalDateTime.of(2025, 12, 31, 12, 0, 0));
        booking.setItem(bookedItem);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    long userId = invocationOnMock.getArgument(0);
                    User booker = new User();
                    booker.setId(userId);
                    booker.setName("booker");
                    booker.setEmail("booker@email.com");
                    return Optional.of(booker);
                });
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    long itemId = invocationOnMock.getArgument(0);
                    Item item = new Item();
                    item.setId(itemId);
                    item.setName("item");
                    item.setDescription("description");
                    item.setAvailable(false);
                    User owner = new User();
                    owner.setId(1L);
                    owner.setName("item_owner");
                    owner.setEmail("item_owner@email.com");
                    item.setOwner(owner);
                    return Optional.of(item);
                });

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> bookingService.create(booking, 2L));

        assertEquals("Item is not available", exception.getMessage());
    }

    @Test
    public void createExceptionTest4 () {
        BookingService bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);
        Item bookedItem = new Item();
        bookedItem.setId(1L);
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStartDate(LocalDateTime.of(2025, 2, 13, 12, 0, 0));
        booking.setEndDate(LocalDateTime.of(2025, 12, 31, 12, 0, 0));
        booking.setItem(bookedItem);
        User booker = new User();
        booker.setId(2L);
        booker.setName("booker");
        booker.setEmail("booker@email.com");

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    return Optional.of(booker);
                });
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    long itemId = invocationOnMock.getArgument(0);
                    Item item = new Item();
                    item.setId(itemId);
                    item.setName("item");
                    item.setDescription("description");
                    item.setAvailable(true);
                    item.setOwner(booker);
                    return Optional.of(item);
                });

        ForbiddenException exception = assertThrows(ForbiddenException.class,
                () -> bookingService.create(booking, 2L));

        assertEquals("Wrong owner", exception.getMessage());
    }

    @Test
    public void updateTest() {
        BookingService bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);

        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    long bookingId = invocationOnMock.getArgument(0);
                    Booking booking = new Booking();
                    booking.setId(bookingId);
                    booking.setStartDate(LocalDateTime.of(2025, 2, 13, 12, 0, 0));
                    booking.setEndDate(LocalDateTime.of(2025, 12, 31, 12, 0, 0));
                    User booker = new User();
                    booker.setId(2L);
                    booker.setName("booker");
                    booker.setEmail("booker@email.com");
                    booking.setUser(booker);
                    Item item = new Item();
                    item.setId(1L);
                    item.setName("item");
                    item.setDescription("description");
                    item.setAvailable(true);
                    User owner = new User();
                    owner.setId(1L);
                    owner.setName("item_owner");
                    owner.setEmail("item_owner@email.com");
                    item.setOwner(owner);
                    booking.setItem(item);
                    return Optional.of(booking);
                });
        Mockito.when(bookingRepository.save(Mockito.any(Booking.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        Booking updatedBooking = bookingService.update(1L, 1L, true);

        assertEquals(1, updatedBooking.getId());
        assertEquals(LocalDateTime.of(2025, 2, 13, 12, 0, 0),
                updatedBooking.getStartDate());
        assertEquals(LocalDateTime.of(2025, 12, 31, 12, 0, 0),
                updatedBooking.getEndDate());
        assertEquals(BookingStatus.APPROVED, updatedBooking.getStatus());
        assertEquals(2, updatedBooking.getUser().getId());
        assertEquals("booker", updatedBooking.getUser().getName());
        assertEquals("booker@email.com", updatedBooking.getUser().getEmail());
        assertEquals(1, updatedBooking.getItem().getId());
        assertEquals("item", updatedBooking.getItem().getName());
        assertEquals("description", updatedBooking.getItem().getDescription());
        assertEquals(true, updatedBooking.getItem().getAvailable());
        assertEquals(1, updatedBooking.getItem().getOwner().getId());
        assertEquals("item_owner", updatedBooking.getItem().getOwner().getName());
        assertEquals("item_owner@email.com", updatedBooking.getItem().getOwner().getEmail());
    }

    @Test
    public void updateTest1() {
        BookingService bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);

        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    long bookingId = invocationOnMock.getArgument(0);
                    Booking booking = new Booking();
                    booking.setId(bookingId);
                    booking.setStartDate(LocalDateTime.of(2025, 2, 13, 12, 0, 0));
                    booking.setEndDate(LocalDateTime.of(2025, 12, 31, 12, 0, 0));
                    User booker = new User();
                    booker.setId(2L);
                    booker.setName("booker");
                    booker.setEmail("booker@email.com");
                    booking.setUser(booker);
                    Item item = new Item();
                    item.setId(1L);
                    item.setName("item");
                    item.setDescription("description");
                    item.setAvailable(true);
                    User owner = new User();
                    owner.setId(1L);
                    owner.setName("item_owner");
                    owner.setEmail("item_owner@email.com");
                    item.setOwner(owner);
                    booking.setItem(item);
                    return Optional.of(booking);
                });
        Mockito.when(bookingRepository.save(Mockito.any(Booking.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        Booking updatedBooking = bookingService.update(1L, 1L, false);

        assertEquals(1, updatedBooking.getId());
        assertEquals(LocalDateTime.of(2025, 2, 13, 12, 0, 0),
                updatedBooking.getStartDate());
        assertEquals(LocalDateTime.of(2025, 12, 31, 12, 0, 0),
                updatedBooking.getEndDate());
        assertEquals(BookingStatus.REJECTED, updatedBooking.getStatus());
        assertEquals(2, updatedBooking.getUser().getId());
        assertEquals("booker", updatedBooking.getUser().getName());
        assertEquals("booker@email.com", updatedBooking.getUser().getEmail());
        assertEquals(1, updatedBooking.getItem().getId());
        assertEquals("item", updatedBooking.getItem().getName());
        assertEquals("description", updatedBooking.getItem().getDescription());
        assertEquals(true, updatedBooking.getItem().getAvailable());
        assertEquals(1, updatedBooking.getItem().getOwner().getId());
        assertEquals("item_owner", updatedBooking.getItem().getOwner().getName());
        assertEquals("item_owner@email.com", updatedBooking.getItem().getOwner().getEmail());
    }

    @Test
    public void updateExceptionTest1() {
        BookingService bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);

        Mockito.when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.update(1L, 1L, true));

        assertEquals("Booking does not exist", exception.getMessage());
    }

    @Test
    public void updateException2() {
        BookingService bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);

        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    long bookingId = invocationOnMock.getArgument(0);
                    Booking booking = new Booking();
                    booking.setId(bookingId);
                    booking.setStartDate(LocalDateTime.of(2025, 2, 13, 12, 0, 0));
                    booking.setEndDate(LocalDateTime.of(2025, 12, 31, 12, 0, 0));
                    User booker = new User();
                    booker.setId(2L);
                    booker.setName("booker");
                    booker.setEmail("booker@email.com");
                    booking.setUser(booker);
                    Item item = new Item();
                    item.setId(1L);
                    item.setName("item");
                    item.setDescription("description");
                    item.setAvailable(true);
                    User owner = new User();
                    owner.setId(3L);
                    owner.setName("item_owner");
                    owner.setEmail("item_owner@email.com");
                    item.setOwner(owner);
                    booking.setItem(item);
                    return Optional.of(booking);
                });

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.update(1L, 1L, true));

        assertEquals("Wrong owner", exception.getMessage());
    }

    @Test
    public void findByIdTest() {
        BookingService bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    long userId = invocationOnMock.getArgument(0);
                    User user = new User();
                    user.setId(userId);
                    user.setName("user");
                    user.setEmail("user@email.com");
                    return Optional.of(user);
                });
        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    long bookingId = invocationOnMock.getArgument(0);
                    Booking booking = new Booking();
                    booking.setId(bookingId);
                    booking.setStartDate(LocalDateTime.of(2025, 2, 13, 12, 0, 0));
                    booking.setEndDate(LocalDateTime.of(2025, 12, 31, 12, 0, 0));
                    booking.setStatus(BookingStatus.APPROVED);
                    User booker = new User();
                    booker.setId(2L);
                    booker.setName("booker");
                    booker.setEmail("booker@email.com");
                    booking.setUser(booker);
                    Item item = new Item();
                    item.setId(1L);
                    item.setName("item");
                    item.setDescription("description");
                    item.setAvailable(true);
                    User owner = new User();
                    owner.setId(1L);
                    owner.setName("item_owner");
                    owner.setEmail("item_owner@email.com");
                    item.setOwner(owner);
                    booking.setItem(item);
                    return Optional.of(booking);
                });

        Booking foundBooking = bookingService.findById(1, 3L);

        assertEquals(1, foundBooking.getId());
        assertEquals(LocalDateTime.of(2025, 2, 13, 12, 0, 0),
                foundBooking.getStartDate());
        assertEquals(LocalDateTime.of(2025, 12, 31, 12, 0, 0),
                foundBooking.getEndDate());
        assertEquals(BookingStatus.APPROVED, foundBooking.getStatus());
        assertEquals(2, foundBooking.getUser().getId());
        assertEquals("booker", foundBooking.getUser().getName());
        assertEquals("booker@email.com", foundBooking.getUser().getEmail());
        assertEquals(1, foundBooking.getItem().getId());
        assertEquals("item", foundBooking.getItem().getName());
        assertEquals("description", foundBooking.getItem().getDescription());
        assertEquals(true, foundBooking.getItem().getAvailable());
        assertEquals(1, foundBooking.getItem().getOwner().getId());
        assertEquals("item_owner", foundBooking.getItem().getOwner().getName());
        assertEquals("item_owner@email.com", foundBooking.getItem().getOwner().getEmail());
    }

    @Test
    public void findByIdExceptionTest() {
        BookingService bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    long userId = invocationOnMock.getArgument(0);
                    User user = new User();
                    user.setId(userId);
                    user.setName("user");
                    user.setEmail("user@email.com");
                    return Optional.of(user);
                });
        Mockito.when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                ()-> bookingService.findById(1, 3L));

        assertEquals("Booking not found", exception.getMessage());
    }

    @Test
    public void findAllByOwnerAllTest() {
        BookingService bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);
        setForOwnerTest();

        mockitoSettingForOwnerTest();
        Mockito.when(bookingRepository.findAllByItem_Owner_IdOrderByStartDateDesc(Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    long ownerId = invocationOnMock.getArgument(0);
                    return bookings.stream()
                            .filter(booking -> booking.getItem().getOwner().getId().equals(ownerId))
                            .sorted(new BookingStartDateComparator())
                            .toList();
                });

        bookingList = bookingService.findAllByOwner(1L, BookingState.ALL);

        checkForOwnerTest();
    }

    @Test
    public void findAllByOwnerPastTest() {
        BookingService bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);
        setForOwnerTest();

        mockitoSettingForOwnerTest();
        Mockito.when(bookingRepository.findAllByItem_Owner_IdAndStartDateBeforeOrderByStartDateDesc(Mockito.anyLong(),
                Mockito.any(LocalDateTime.class)))
                .thenAnswer(invocationOnMock -> {
                    long ownerId = invocationOnMock.getArgument(0);
                    LocalDateTime date =  invocationOnMock.getArgument(1);
                    return bookings.stream()
                            .filter(booking -> booking.getItem().getOwner().getId().equals(ownerId))
                            .filter(booking -> booking.getStartDate().isBefore(date))
                            .sorted(new BookingStartDateComparator())
                            .toList();
                });

        bookingList = bookingService.findAllByOwner(1L, BookingState.PAST);

        checkForOwnerTest();
    }

    @Test
    public void findAllByOwnerFutureTest() {
        BookingService bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);
        setForOwnerTest();

        mockitoSettingForOwnerTest();
        Mockito.when(bookingRepository.findAllByItem_Owner_IdAndEndDateAfterOrderByStartDateDesc(Mockito.anyLong(),
                        Mockito.any(LocalDateTime.class)))
                .thenAnswer(invocationOnMock -> {
                    long ownerId = invocationOnMock.getArgument(0);
                    LocalDateTime date =  invocationOnMock.getArgument(1);
                    return bookings.stream()
                            .filter(booking -> booking.getItem().getOwner().getId().equals(ownerId))
                            .filter(booking -> booking.getEndDate().isAfter(date))
                            .sorted(new BookingStartDateComparator())
                            .toList();
                });

        bookingList = bookingService.findAllByOwner(1L, BookingState.FUTURE);

        checkForOwnerTest();
    }

    @Test
    public void findAllByOwnerCurrentTest() {
        BookingService bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);
        setForOwnerTest();

        mockitoSettingForOwnerTest();
        Mockito.when(bookingRepository.findAllByItem_Owner_IdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(Mockito.anyLong(),
                        Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class)))
                .thenAnswer(invocationOnMock -> {
                    long ownerId = invocationOnMock.getArgument(0);
                    LocalDateTime date1 =  invocationOnMock.getArgument(1);
                    LocalDateTime date2 = invocationOnMock.getArgument(2);
                    return bookings.stream()
                            .filter(booking -> booking.getItem().getOwner().getId().equals(ownerId))
                            .filter(booking -> booking.getStartDate().isBefore(date1))
                            .filter(booking -> booking.getEndDate().isAfter(date2))
                            .sorted(new BookingStartDateComparator())
                            .toList();
                });

        bookingList = bookingService.findAllByOwner(1L, BookingState.CURRENT);

        checkForOwnerTest();
    }

    @Test
    public void findAllByOwnerWaitingTest() {
        BookingService bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);
        setForOwnerTest();

        mockitoSettingForOwnerTest();
        Mockito.when(bookingRepository.findAllByItem_Owner_IdAndStatusIsOrderByStartDateDesc(Mockito.anyLong(),
                        Mockito.any(BookingStatus.class)))
                .thenAnswer(invocationOnMock -> {
                    long ownerId = invocationOnMock.getArgument(0);
                    BookingStatus bookingStatus = invocationOnMock.getArgument(1);
                    return bookings.stream()
                            .filter(booking -> booking.getItem().getOwner().getId().equals(ownerId))
                            .filter(booking -> booking.getStatus().equals(bookingStatus))
                            .sorted(new BookingStartDateComparator())
                            .toList();
                });

        bookingList = bookingService.findAllByOwner(1L, BookingState.WAITING);

        assertEquals(1, bookingList.size());
        assertEquals(1, bookingList.get(0).getId());
        assertEquals(LocalDateTime.of(2025, 2, 13, 12, 0, 0),
                bookingList.get(0).getStartDate());
        assertEquals(LocalDateTime.of(NEXT_YEAR, 12, 31, 12, 0, 0),
                bookingList.get(0).getEndDate());
        assertEquals(BookingStatus.WAITING, bookings.get(0).getStatus());
        assertEquals(3, bookingList.get(0).getUser().getId());
        assertEquals("booker", bookingList.get(0).getUser().getName());
        assertEquals("booker@email.com", bookingList.get(0).getUser().getEmail());
        assertEquals(1, bookingList.get(0).getItem().getId());
        assertEquals("item1", bookingList.get(0).getItem().getName());
        assertEquals("description1", bookingList.get(0).getItem().getDescription());
        assertEquals(true, bookingList.get(0).getItem().getAvailable());
        assertEquals(1, bookingList.get(0).getItem().getOwner().getId());
        assertEquals("owner1", bookingList.get(0).getItem().getOwner().getName());
        assertEquals("owner1@email.com", bookingList.get(0).getItem().getOwner().getEmail());
    }

    @Test
    public void findAllByOwnerRejectedTest() {
        BookingService bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);
        setForOwnerTest();

        mockitoSettingForOwnerTest();
        Mockito.when(bookingRepository.findAllByItem_Owner_IdAndStatusIsOrderByStartDateDesc(Mockito.anyLong(),
                        Mockito.any(BookingStatus.class)))
                .thenAnswer(invocationOnMock -> {
                    long ownerId = invocationOnMock.getArgument(0);
                    BookingStatus bookingStatus = invocationOnMock.getArgument(1);
                    return bookings.stream()
                            .filter(booking -> booking.getItem().getOwner().getId().equals(ownerId))
                            .filter(booking -> booking.getStatus().equals(bookingStatus))
                            .sorted(new BookingStartDateComparator())
                            .toList();
                });

        bookingList = bookingService.findAllByOwner(1L, BookingState.REJECTED);

        assertEquals(1, bookingList.size());
        assertEquals(3, bookingList.get(0).getId());
        assertEquals(LocalDateTime.of(2025, 2, 9, 12, 0, 0),
                bookingList.get(0).getStartDate());
        assertEquals(LocalDateTime.of(NEXT_YEAR, 12, 31, 12, 0, 0),
                bookingList.get(0).getEndDate());
        assertEquals(BookingStatus.REJECTED, bookingList.get(0).getStatus());
        assertEquals(3, bookingList.get(0).getUser().getId());
        assertEquals("booker", bookingList.get(0).getUser().getName());
        assertEquals("booker@email.com", bookingList.get(0).getUser().getEmail());
        assertEquals(3, bookingList.get(0).getItem().getId());
        assertEquals("item3", bookingList.get(0).getItem().getName());
        assertEquals("description3", bookingList.get(0).getItem().getDescription());
        assertEquals(true, bookingList.get(0).getItem().getAvailable());
        assertEquals(1, bookingList.get(0).getItem().getOwner().getId());
        assertEquals("owner1", bookingList.get(0).getItem().getOwner().getName());
        assertEquals("owner1@email.com", bookingList.get(0).getItem().getOwner().getEmail());
    }

    @Test
    public void findAllByOwnerDefaultTest() {
        BookingService bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);
        setForOwnerTest();

        mockitoSettingForOwnerTest();

        bookingList = bookingService.findAllByOwner(1L, BookingState.ELSE);

        assertEquals(0, bookingList.size());
    }

    @Test
    public void findAllByOwnerExceptionTest() {
        BookingService bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        ForbiddenException exception = assertThrows(ForbiddenException.class,
                () -> bookingService.findAllByOwner(1L, BookingState.ALL));

        assertEquals("Wrong user", exception.getMessage());
    }

    @Test
    public void findAllByBookerAllTest() {
        BookingService bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);
        setForBookerTest();

        mockitoSettingForBookerTest();
        Mockito.when(bookingRepository.findAllByUser_IdOrderByStartDateDesc(Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    long bookerId = invocationOnMock.getArgument(0);
                    return bookings.stream()
                            .filter(booking -> booking.getUser().getId().equals(bookerId))
                            .sorted(new BookingStartDateComparator())
                            .toList();
                });

        bookingList = bookingService.findAllByBooker(1L, BookingState.ALL);

        checkForBookerTest();
    }

    @Test
    public void findAllByBookerPastTest() {
        BookingService bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);
        setForBookerTest();

        mockitoSettingForBookerTest();
        Mockito.when(bookingRepository.findAllByUser_IdAndStartDateBeforeOrderByStartDateDesc(Mockito.anyLong(),
                        Mockito.any(LocalDateTime.class)))
                .thenAnswer(invocationOnMock -> {
                    long bookerId = invocationOnMock.getArgument(0);
                    LocalDateTime date =  invocationOnMock.getArgument(1);
                    return bookings.stream()
                            .filter(booking -> booking.getUser().getId().equals(bookerId))
                            .filter(booking -> booking.getStartDate().isBefore(date))
                            .sorted(new BookingStartDateComparator())
                            .toList();
                });

        bookingList = bookingService.findAllByBooker(1L, BookingState.PAST);

        checkForBookerTest();
    }

    @Test
    public void findAllByBookerFutureTest() {
        BookingService bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);
        setForBookerTest();

        mockitoSettingForBookerTest();
        Mockito.when(bookingRepository.findAllByUser_IdAndEndDateAfterOrderByStartDateDesc(Mockito.anyLong(),
                        Mockito.any(LocalDateTime.class)))
                .thenAnswer(invocationOnMock -> {
                    long bookerId = invocationOnMock.getArgument(0);
                    LocalDateTime date =  invocationOnMock.getArgument(1);
                    return bookings.stream()
                            .filter(booking -> booking.getUser().getId().equals(bookerId))
                            .filter(booking -> booking.getEndDate().isAfter(date))
                            .sorted(new BookingStartDateComparator())
                            .toList();
                });

        bookingList = bookingService.findAllByBooker(1L, BookingState.FUTURE);

        checkForBookerTest();
    }

    @Test
    public void findAllByBookerCurrentTest() {
        BookingService bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);
        setForBookerTest();

        mockitoSettingForBookerTest();
        Mockito.when(bookingRepository.findAllByUser_IdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(Mockito.anyLong(),
                        Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class)))
                .thenAnswer(invocationOnMock -> {
                    long bookerId = invocationOnMock.getArgument(0);
                    LocalDateTime date1 =  invocationOnMock.getArgument(1);
                    LocalDateTime date2 = invocationOnMock.getArgument(2);
                    return bookings.stream()
                            .filter(booking -> booking.getUser().getId().equals(bookerId))
                            .filter(booking -> booking.getStartDate().isBefore(date1))
                            .filter(booking -> booking.getEndDate().isAfter(date2))
                            .sorted(new BookingStartDateComparator())
                            .toList();
                });

        bookingList = bookingService.findAllByBooker(1L, BookingState.CURRENT);

        checkForBookerTest();
    }

    @Test
    public void findAllByBookerWaitingTest() {
        BookingService bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);
        setForBookerTest();

        mockitoSettingForBookerTest();
        Mockito.when(bookingRepository.findAllByUser_IdAndStatusIsOrderByStartDateDesc(
                Mockito.anyLong(), Mockito.any(BookingStatus.class)))
                .thenAnswer(invocationOnMock -> {
                    long bookerId = invocationOnMock.getArgument(0);
                    BookingStatus bookingStatus = invocationOnMock.getArgument(1);
                    return bookings.stream()
                            .filter(booking -> booking.getUser().getId().equals(bookerId))
                            .filter(booking -> booking.getStatus().equals(bookingStatus))
                            .sorted(new BookingStartDateComparator())
                            .toList();
                });

        bookingList = bookingService.findAllByBooker(1L, BookingState.WAITING);

        assertEquals(1, bookingList.size());
        assertEquals(1, bookingList.get(0).getId());
        assertEquals(LocalDateTime.of(2025, 2, 13, 12, 0, 0),
                bookingList.get(0).getStartDate());
        assertEquals(LocalDateTime.of(NEXT_YEAR, 12, 31, 12, 0, 0),
                bookingList.get(0).getEndDate());
        assertEquals(BookingStatus.WAITING, bookings.get(0).getStatus());
        assertEquals(1, bookingList.get(0).getUser().getId());
        assertEquals("booker1", bookingList.get(0).getUser().getName());
        assertEquals("booker1@email.com", bookingList.get(0).getUser().getEmail());
        assertEquals(1, bookingList.get(0).getItem().getId());
        assertEquals("item1", bookingList.get(0).getItem().getName());
        assertEquals("description1", bookingList.get(0).getItem().getDescription());
        assertEquals(true, bookingList.get(0).getItem().getAvailable());
        assertEquals(3, bookingList.get(0).getItem().getOwner().getId());
        assertEquals("owner", bookingList.get(0).getItem().getOwner().getName());
        assertEquals("owner@email.com", bookingList.get(0).getItem().getOwner().getEmail());
    }

    @Test
    public void findAllByBookerRejectedTest() {
        BookingService bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);
        setForBookerTest();

        mockitoSettingForBookerTest();
        Mockito.when(bookingRepository.findAllByUser_IdAndStatusIsOrderByStartDateDesc(
                        Mockito.anyLong(), Mockito.any(BookingStatus.class)))
                .thenAnswer(invocationOnMock -> {
                    long bookerId = invocationOnMock.getArgument(0);
                    BookingStatus bookingStatus = invocationOnMock.getArgument(1);
                    return bookings.stream()
                            .filter(booking -> booking.getUser().getId().equals(bookerId))
                            .filter(booking -> booking.getStatus().equals(bookingStatus))
                            .sorted(new BookingStartDateComparator())
                            .toList();
                });

        bookingList = bookingService.findAllByBooker(1L, BookingState.REJECTED);

        assertEquals(1, bookingList.size());
        assertEquals(3, bookingList.get(0).getId());
        assertEquals(LocalDateTime.of(2025, 2, 9, 12, 0, 0),
                bookingList.get(0).getStartDate());
        assertEquals(LocalDateTime.of(NEXT_YEAR, 12, 31, 12, 0, 0),
                bookingList.get(0).getEndDate());
        assertEquals(BookingStatus.REJECTED, bookingList.get(0).getStatus());
        assertEquals(1, bookingList.get(0).getUser().getId());
        assertEquals("booker1", bookingList.get(0).getUser().getName());
        assertEquals("booker1@email.com", bookingList.get(0).getUser().getEmail());
        assertEquals(3, bookingList.get(0).getItem().getId());
        assertEquals("item3", bookingList.get(0).getItem().getName());
        assertEquals("description3", bookingList.get(0).getItem().getDescription());
        assertEquals(true, bookingList.get(0).getItem().getAvailable());
        assertEquals(3, bookingList.get(0).getItem().getOwner().getId());
        assertEquals("owner", bookingList.get(0).getItem().getOwner().getName());
        assertEquals("owner@email.com", bookingList.get(0).getItem().getOwner().getEmail());
    }

    @Test
    public void findAllByBookerDefaultTest() {
        BookingService bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);
        setForBookerTest();

        mockitoSettingForBookerTest();

        bookingList = bookingService.findAllByBooker(1L, BookingState.ELSE);

        assertEquals(0, bookingList.size());
    }

    private void setForOwnerTest() {
        User owner1 = new User();
        owner1.setId(1L);
        owner1.setName("owner1");
        owner1.setEmail("owner1@email.com");
        User owner2 = new User();
        owner2.setId(2L);
        owner2.setName("owner2");
        owner2.setEmail("owner2@email.com");
        owners = List.of(owner1, owner2);
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("item1");
        item1.setDescription("description1");
        item1.setAvailable(true);
        item1.setOwner(owner1);
        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("item2");
        item2.setDescription("description2");
        item2.setAvailable(true);
        item2.setOwner(owner2);
        Item item3 = new Item();
        item3.setId(3L);
        item3.setName("item3");
        item3.setDescription("description3");
        item3.setAvailable(true);
        item3.setOwner(owner1);
        User booker = new User();
        booker.setId(3L);
        booker.setName("booker");
        booker.setEmail("booker@email.com");
        Booking booking1 = new Booking();
        booking1.setId(1L);
        booking1.setStartDate(LocalDateTime.of(2025, 2, 13, 12, 0, 0));
        booking1.setEndDate(LocalDateTime.of(NEXT_YEAR, 12, 31, 12, 0, 0));
        booking1.setStatus(BookingStatus.WAITING);
        booking1.setItem(item1);
        booking1.setUser(booker);
        Booking booking2 = new Booking();
        booking2.setId(2L);
        booking2.setStartDate(LocalDateTime.of(2025, 2, 10, 12, 0, 0));
        booking2.setEndDate(LocalDateTime.of(NEXT_YEAR, 12, 31, 12, 0, 0));
        booking2.setStatus(BookingStatus.APPROVED);
        booking2.setItem(item2);
        booking2.setUser(booker);
        Booking booking3 = new Booking();
        booking3.setId(3L);
        booking3.setStartDate(LocalDateTime.of(2025, 2, 9, 12, 0, 0));
        booking3.setEndDate(LocalDateTime.of(NEXT_YEAR, 12, 31, 12, 0, 0));
        booking3.setStatus(BookingStatus.REJECTED);
        booking3.setItem(item3);
        booking3.setUser(booker);
        Booking booking4 = new Booking();
        booking4.setId(4L);
        booking4.setStartDate(LocalDateTime.of(2025, 2, 8, 12, 0, 0));
        booking4.setEndDate(LocalDateTime.of(NEXT_YEAR, 12, 31, 12, 0, 0));
        booking4.setStatus(BookingStatus.APPROVED);
        booking4.setItem(item3);
        booking4.setUser(booker);
        bookings = List.of(booking1, booking2, booking3, booking4);
    }

    private void setForBookerTest() {
        User owner = new User();
        owner.setId(3L);
        owner.setName("owner");
        owner.setEmail("owner@email.com");
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("item1");
        item1.setDescription("description1");
        item1.setAvailable(true);
        item1.setOwner(owner);
        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("item2");
        item2.setDescription("description2");
        item2.setAvailable(true);
        item2.setOwner(owner);
        Item item3 = new Item();
        item3.setId(3L);
        item3.setName("item3");
        item3.setDescription("description3");
        item3.setAvailable(true);
        item3.setOwner(owner);
        User booker1 = new User();
        booker1.setId(1L);
        booker1.setName("booker1");
        booker1.setEmail("booker1@email.com");
        User booker2 = new User();
        booker2.setId(2L);
        booker2.setName("booker2");
        booker2.setEmail("booker2@email.com");
        bookers = List.of(booker1, booker2);
        Booking booking1 = new Booking();
        booking1.setId(1L);
        booking1.setStartDate(LocalDateTime.of(2025, 2, 13, 12, 0, 0));
        booking1.setEndDate(LocalDateTime.of(NEXT_YEAR, 12, 31, 12, 0, 0));
        booking1.setStatus(BookingStatus.WAITING);
        booking1.setItem(item1);
        booking1.setUser(booker1);
        Booking booking2 = new Booking();
        booking2.setId(2L);
        booking2.setStartDate(LocalDateTime.of(2025, 2, 10, 12, 0, 0));
        booking2.setEndDate(LocalDateTime.of(NEXT_YEAR, 12, 31, 12, 0, 0));
        booking2.setStatus(BookingStatus.APPROVED);
        booking2.setItem(item2);
        booking2.setUser(booker2);
        Booking booking3 = new Booking();
        booking3.setId(3L);
        booking3.setStartDate(LocalDateTime.of(2025, 2, 9, 12, 0, 0));
        booking3.setEndDate(LocalDateTime.of(NEXT_YEAR, 12, 31, 12, 0, 0));
        booking3.setStatus(BookingStatus.REJECTED);
        booking3.setItem(item3);
        booking3.setUser(booker1);
        Booking booking4 = new Booking();
        booking4.setId(4L);
        booking4.setStartDate(LocalDateTime.of(2025, 2, 8, 12, 0, 0));
        booking4.setEndDate(LocalDateTime.of(NEXT_YEAR, 12, 31, 12, 0, 0));
        booking4.setStatus(BookingStatus.APPROVED);
        booking4.setItem(item2);
        booking4.setUser(booker1);
        bookings = List.of(booking1, booking2, booking3, booking4);
    }

    private void checkForOwnerTest() {
        assertEquals(3, bookingList.size());
        assertEquals(1, bookingList.get(0).getId());
        assertEquals(LocalDateTime.of(2025, 2, 13, 12, 0, 0),
                bookingList.get(0).getStartDate());
        assertEquals(LocalDateTime.of(NEXT_YEAR, 12, 31, 12, 0, 0),
                bookingList.get(0).getEndDate());
        assertEquals(BookingStatus.WAITING, bookings.get(0).getStatus());
        assertEquals(3, bookingList.get(0).getUser().getId());
        assertEquals("booker", bookingList.get(0).getUser().getName());
        assertEquals("booker@email.com", bookingList.get(0).getUser().getEmail());
        assertEquals(1, bookingList.get(0).getItem().getId());
        assertEquals("item1", bookingList.get(0).getItem().getName());
        assertEquals("description1", bookingList.get(0).getItem().getDescription());
        assertEquals(true, bookingList.get(0).getItem().getAvailable());
        assertEquals(1, bookingList.get(0).getItem().getOwner().getId());
        assertEquals("owner1", bookingList.get(0).getItem().getOwner().getName());
        assertEquals("owner1@email.com", bookingList.get(0).getItem().getOwner().getEmail());
        assertEquals(3, bookingList.get(1).getId());
        assertEquals(LocalDateTime.of(2025, 2, 9, 12, 0, 0),
                bookingList.get(1).getStartDate());
        assertEquals(LocalDateTime.of(NEXT_YEAR, 12, 31, 12, 0, 0),
                bookingList.get(1).getEndDate());
        assertEquals(BookingStatus.REJECTED, bookingList.get(1).getStatus());
        assertEquals(3, bookingList.get(1).getUser().getId());
        assertEquals("booker", bookingList.get(1).getUser().getName());
        assertEquals("booker@email.com", bookingList.get(1).getUser().getEmail());
        assertEquals(3, bookingList.get(1).getItem().getId());
        assertEquals("item3", bookingList.get(1).getItem().getName());
        assertEquals("description3", bookingList.get(1).getItem().getDescription());
        assertEquals(true, bookingList.get(1).getItem().getAvailable());
        assertEquals(1, bookingList.get(1).getItem().getOwner().getId());
        assertEquals("owner1", bookingList.get(1).getItem().getOwner().getName());
        assertEquals("owner1@email.com", bookingList.get(1).getItem().getOwner().getEmail());
        assertEquals(4, bookingList.get(2).getId());
        assertEquals(LocalDateTime.of(2025, 2, 8, 12, 0, 0),
                bookingList.get(2).getStartDate());
        assertEquals(LocalDateTime.of(NEXT_YEAR, 12, 31, 12, 0, 0),
                bookingList.get(2).getEndDate());
        assertEquals(BookingStatus.APPROVED, bookingList.get(2).getStatus());
        assertEquals(3, bookingList.get(2).getUser().getId());
        assertEquals("booker", bookingList.get(2).getUser().getName());
        assertEquals("booker@email.com", bookingList.get(2).getUser().getEmail());
        assertEquals(3, bookingList.get(2).getItem().getId());
        assertEquals("item3", bookingList.get(2).getItem().getName());
        assertEquals("description3", bookingList.get(2).getItem().getDescription());
        assertEquals(true, bookingList.get(2).getItem().getAvailable());
        assertEquals(1, bookingList.get(2).getItem().getOwner().getId());
        assertEquals("owner1", bookingList.get(2).getItem().getOwner().getName());
        assertEquals("owner1@email.com", bookingList.get(2).getItem().getOwner().getEmail());
    }

    private void checkForBookerTest() {
        assertEquals(3, bookingList.size());
        assertEquals(1, bookingList.get(0).getId());
        assertEquals(LocalDateTime.of(2025, 2, 13, 12, 0, 0),
                bookingList.get(0).getStartDate());
        assertEquals(LocalDateTime.of(NEXT_YEAR, 12, 31, 12, 0, 0),
                bookingList.get(0).getEndDate());
        assertEquals(BookingStatus.WAITING, bookings.get(0).getStatus());
        assertEquals(1, bookingList.get(0).getUser().getId());
        assertEquals("booker1", bookingList.get(0).getUser().getName());
        assertEquals("booker1@email.com", bookingList.get(0).getUser().getEmail());
        assertEquals(1, bookingList.get(0).getItem().getId());
        assertEquals("item1", bookingList.get(0).getItem().getName());
        assertEquals("description1", bookingList.get(0).getItem().getDescription());
        assertEquals(true, bookingList.get(0).getItem().getAvailable());
        assertEquals(3, bookingList.get(0).getItem().getOwner().getId());
        assertEquals("owner", bookingList.get(0).getItem().getOwner().getName());
        assertEquals("owner@email.com", bookingList.get(0).getItem().getOwner().getEmail());
        assertEquals(3, bookingList.get(1).getId());
        assertEquals(LocalDateTime.of(2025, 2, 9, 12, 0, 0),
                bookingList.get(1).getStartDate());
        assertEquals(LocalDateTime.of(NEXT_YEAR, 12, 31, 12, 0, 0),
                bookingList.get(1).getEndDate());
        assertEquals(BookingStatus.REJECTED, bookingList.get(1).getStatus());
        assertEquals(1, bookingList.get(1).getUser().getId());
        assertEquals("booker1", bookingList.get(1).getUser().getName());
        assertEquals("booker1@email.com", bookingList.get(1).getUser().getEmail());
        assertEquals(3, bookingList.get(1).getItem().getId());
        assertEquals("item3", bookingList.get(1).getItem().getName());
        assertEquals("description3", bookingList.get(1).getItem().getDescription());
        assertEquals(true, bookingList.get(1).getItem().getAvailable());
        assertEquals(3, bookingList.get(1).getItem().getOwner().getId());
        assertEquals("owner", bookingList.get(1).getItem().getOwner().getName());
        assertEquals("owner@email.com", bookingList.get(1).getItem().getOwner().getEmail());
        assertEquals(4, bookingList.get(2).getId());
        assertEquals(LocalDateTime.of(2025, 2, 8, 12, 0, 0),
                bookingList.get(2).getStartDate());
        assertEquals(LocalDateTime.of(NEXT_YEAR, 12, 31, 12, 0, 0),
                bookingList.get(2).getEndDate());
        assertEquals(BookingStatus.APPROVED, bookingList.get(2).getStatus());
        assertEquals(1, bookingList.get(2).getUser().getId());
        assertEquals("booker1", bookingList.get(2).getUser().getName());
        assertEquals("booker1@email.com", bookingList.get(2).getUser().getEmail());
        assertEquals(2, bookingList.get(2).getItem().getId());
        assertEquals("item2", bookingList.get(2).getItem().getName());
        assertEquals("description2", bookingList.get(2).getItem().getDescription());
        assertEquals(true, bookingList.get(2).getItem().getAvailable());
        assertEquals(3, bookingList.get(2).getItem().getOwner().getId());
        assertEquals("owner", bookingList.get(2).getItem().getOwner().getName());
        assertEquals("owner@email.com", bookingList.get(2).getItem().getOwner().getEmail());
    }

    private void mockitoSettingForOwnerTest() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    long ownerId = invocationOnMock.getArgument(0);
                    return owners.stream()
                            .filter(owner -> owner.getId().equals(ownerId))
                            .findFirst();
                });
    }

    private void mockitoSettingForBookerTest() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    long bookerId = invocationOnMock.getArgument(0);
                    return bookers.stream()
                            .filter(booker -> booker.getId().equals(bookerId))
                            .findFirst();
                });
    }
}
