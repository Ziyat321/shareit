package com.practice.shareitziyat.server.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.shareitziyat.server.booking.dto.BookingCreateDto;
import com.practice.shareitziyat.server.booking.dto.BookingMapper;
import com.practice.shareitziyat.server.item.Item;
import com.practice.shareitziyat.server.item.dto.ItemMapper;
import com.practice.shareitziyat.server.user.User;
import com.practice.shareitziyat.server.user.dto.UserMapper;
import com.practice.shareitziyat.server.utils.RequestConstants;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    @SpyBean
    private ItemMapper itemMapper;

    @SpyBean
    private UserMapper userMapper;

    @SpyBean
    private BookingMapper bookingMapper;

    final int CURRENT_YEAR = LocalDateTime.now().getYear();

    final int NEXT_YEAR = CURRENT_YEAR + 1;

    final String NEXT_YEAR_STR = String.valueOf(NEXT_YEAR);

    @Test
    @SneakyThrows
    public void createTest() {
        User booker = new User();
        booker.setId(1L);
        booker.setName("booker");
        booker.setEmail("booker@email.com");

        BookingCreateDto bookingCreate = new BookingCreateDto();
        bookingCreate.setItemId(1L);
        bookingCreate.setStart(LocalDateTime.of(NEXT_YEAR, 2, 17, 12, 0, 0));
        bookingCreate.setEnd(LocalDateTime.of(NEXT_YEAR, 12, 31, 12, 0, 0));

        String bookingJson = objectMapper.writeValueAsString(bookingCreate);

        Mockito.when(bookingService.create(Mockito.any(Booking.class), Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    Booking booking = invocationOnMock.getArgument(0);
                    booking.setId(1L);
                    booking.setUser(booker);
                    Item item = new Item();
                    item.setId(booking.getItem().getId());
                    item.setName("item");
                    item.setDescription("description");
                    item.setAvailable(true);
                    User owner = new User();
                    owner.setId(2L);
                    owner.setName("owner");
                    owner.setEmail("owner@email.com");
                    item.setOwner(owner);
                    booking.setItem(item);
                    booking.setStatus(BookingStatus.WAITING);
                    return booking;
                });

        mockMvc.perform(MockMvcRequestBuilders.post("/bookings")
                        .header(RequestConstants.USER_HEADER, booker.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.start")
                        .value(NEXT_YEAR_STR + "-02-17T12:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.end")
                        .value(NEXT_YEAR_STR + "-12-31T12:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("WAITING"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.booker.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.booker.name").value("booker"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.booker.email").value("booker@email.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.name").value("item"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.description").value("description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.available").value(true));
    }

    @Test
    @SneakyThrows
    public void updateCheck() {
        User owner = new User();
        owner.setId(1L);
        owner.setName("owner");
        owner.setEmail("owner@email.com");

        Mockito.when(bookingService.update(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyBoolean()))
                .thenAnswer(invocationOnMock -> {
                    long bookingId = invocationOnMock.getArgument(0);
                    boolean approved = invocationOnMock.getArgument(2);
                    Booking bookingExisting = new Booking();
                    bookingExisting.setId(bookingId);
                    bookingExisting.setStartDate(LocalDateTime.of(2025, 2, 17, 12, 0, 0));
                    bookingExisting.setEndDate(LocalDateTime.of(2025, 12, 31, 12, 0, 0));
                    bookingExisting.setStatus(BookingStatus.WAITING);
                    User booker = new User();
                    booker.setId(2L);
                    booker.setName("booker");
                    booker.setEmail("booker@email.com");
                    bookingExisting.setUser(booker);
                    Item item = new Item();
                    item.setId(1L);
                    item.setName("item");
                    item.setDescription("description");
                    item.setAvailable(true);
                    item.setOwner(owner);
                    bookingExisting.setItem(item);
                    BookingStatus bookingStatus = approved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
                    bookingExisting.setStatus(bookingStatus);
                    return bookingExisting;
                });

        mockMvc.perform(MockMvcRequestBuilders.patch("/bookings/1?approved=true")
                        .header(RequestConstants.USER_HEADER, owner.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.start")
                        .value("2025-02-17T12:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.end")
                        .value("2025-12-31T12:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("APPROVED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.booker.id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.booker.name").value("booker"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.booker.email").value("booker@email.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.name").value("item"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.description").value("description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.available").value(true));
    }

    @Test
    @SneakyThrows
    public void findByIdTest() {
        User user = new User();
        user.setId(1L);
        user.setName("user");
        user.setEmail("user@email.com");

        Mockito.when(bookingService.findById(Mockito.anyLong(), Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    long bookingId = invocationOnMock.getArgument(0);
                    Booking booking = new Booking();
                    booking.setId(bookingId);
                    booking.setStartDate(LocalDateTime.of(2025, 2, 17, 12, 0, 0));
                    booking.setEndDate(LocalDateTime.of(2025, 12, 31, 12, 0, 0));
                    booking.setStatus(BookingStatus.WAITING);
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
                    owner.setName("owner");
                    owner.setEmail("owner@email.com");
                    item.setOwner(owner);
                    booking.setItem(item);
                    return booking;
                });

        mockMvc.perform(MockMvcRequestBuilders.get("/bookings/1")
                        .header(RequestConstants.USER_HEADER, user.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.start")
                        .value("2025-02-17T12:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.end")
                        .value("2025-12-31T12:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("WAITING"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.booker.id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.booker.name").value("booker"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.booker.email").value("booker@email.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.name").value("item"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.description").value("description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.available").value(true));
    }

    @Test
    @SneakyThrows
    public void findAllByBookerTest() {
        User booker = new User();
        booker.setId(1L);
        booker.setName("booker");
        booker.setEmail("booker@email.com");

        Mockito.when(bookingService.findAllByBooker(Mockito.anyLong(), Mockito.any(BookingState.class), Mockito.anyInt(), Mockito.anyInt()))
                .thenAnswer(invocationOnMock -> {
                    User owner = new User();
                    owner.setId(2L);
                    owner.setName("owner");
                    owner.setEmail("owner@email.com");
                    Booking booking1 = new Booking();
                    booking1.setId(1L);
                    booking1.setStartDate(LocalDateTime.of(2025, 2, 17, 12, 0, 0));
                    booking1.setEndDate(LocalDateTime.of(2025, 12, 31, 12, 0, 0));
                    booking1.setStatus(BookingStatus.APPROVED);
                    booking1.setUser(booker);
                    Item item1 = new Item();
                    item1.setId(1L);
                    item1.setName("item1");
                    item1.setDescription("description1");
                    item1.setAvailable(true);
                    item1.setOwner(owner);
                    booking1.setItem(item1);
                    Booking booking2 = new Booking();
                    booking2.setId(2L);
                    booking2.setStartDate(LocalDateTime.of(2025, 2, 15, 12, 0, 0));
                    booking2.setEndDate(LocalDateTime.of(2025, 12, 31, 12, 0, 0));
                    booking2.setStatus(BookingStatus.WAITING);
                    booking2.setUser(booker);
                    Item item2 = new Item();
                    item2.setId(2L);
                    item2.setName("item2");
                    item2.setDescription("description2");
                    item2.setAvailable(true);
                    item2.setOwner(owner);
                    booking2.setItem(item2);
                    return List.of(booking1, booking2);
                });

        mockMvc.perform(MockMvcRequestBuilders.get("/bookings")
                        .header(RequestConstants.USER_HEADER, booker.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].start")
                        .value("2025-02-17T12:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].end")
                        .value("2025-12-31T12:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status").value("APPROVED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].booker.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].booker.name").value("booker"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].booker.email").value("booker@email.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].item.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].item.name").value("item1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].item.description").value("description1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].item.available").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].start")
                        .value("2025-02-15T12:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].end")
                        .value("2025-12-31T12:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].status").value("WAITING"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].booker.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].booker.name").value("booker"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].booker.email").value("booker@email.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].item.id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].item.name").value("item2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].item.description").value("description2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].item.available").value(true));
    }

    @Test
    @SneakyThrows
    public void findAllByOwnerTest() {
        User owner = new User();
        owner.setId(2L);
        owner.setName("owner");
        owner.setEmail("owner@email.com");

        Mockito.when(bookingService.findAllByOwner(Mockito.anyLong(), Mockito.any(BookingState.class), Mockito.anyInt(), Mockito.anyInt()))
                .thenAnswer(invocationOnMock -> {
                    User booker = new User();
                    booker.setId(1L);
                    booker.setName("booker");
                    booker.setEmail("booker@email.com");
                    Booking booking1 = new Booking();
                    booking1.setId(1L);
                    booking1.setStartDate(LocalDateTime.of(2025, 2, 17, 12, 0, 0));
                    booking1.setEndDate(LocalDateTime.of(2025, 12, 31, 12, 0, 0));
                    booking1.setStatus(BookingStatus.APPROVED);
                    booking1.setUser(booker);
                    Item item1 = new Item();
                    item1.setId(1L);
                    item1.setName("item1");
                    item1.setDescription("description1");
                    item1.setAvailable(true);
                    item1.setOwner(owner);
                    booking1.setItem(item1);
                    Booking booking2 = new Booking();
                    booking2.setId(2L);
                    booking2.setStartDate(LocalDateTime.of(2025, 2, 15, 12, 0, 0));
                    booking2.setEndDate(LocalDateTime.of(2025, 12, 31, 12, 0, 0));
                    booking2.setStatus(BookingStatus.WAITING);
                    booking2.setUser(booker);
                    Item item2 = new Item();
                    item2.setId(2L);
                    item2.setName("item2");
                    item2.setDescription("description2");
                    item2.setAvailable(true);
                    item2.setOwner(owner);
                    booking2.setItem(item2);
                    return List.of(booking1, booking2);
                });

        mockMvc.perform(MockMvcRequestBuilders.get("/bookings/owner")
                        .header(RequestConstants.USER_HEADER, owner.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].start")
                        .value("2025-02-17T12:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].end")
                        .value("2025-12-31T12:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status").value("APPROVED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].booker.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].booker.name").value("booker"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].booker.email").value("booker@email.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].item.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].item.name").value("item1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].item.description").value("description1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].item.available").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].start")
                        .value("2025-02-15T12:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].end")
                        .value("2025-12-31T12:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].status").value("WAITING"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].booker.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].booker.name").value("booker"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].booker.email").value("booker@email.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].item.id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].item.name").value("item2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].item.description").value("description2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].item.available").value(true));
    }
}
