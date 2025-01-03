package com.practice.shareitziyat.item;

import com.practice.shareitziyat.booking.BookingRepository;
import com.practice.shareitziyat.item.dto.ItemMapper;
import com.practice.shareitziyat.request.Request;
import com.practice.shareitziyat.request.RequestRepository;
import com.practice.shareitziyat.user.User;
import com.practice.shareitziyat.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @Mock
    ItemRepository itemRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    CommentRepository commentRepository;

    @Mock
    BookingRepository bookingRepository;

    @Mock
    RequestRepository requestRepository;

    @Spy
    ItemMapper itemMapper;


    @Test
    public void createTest() {
        ItemService itemService = new ItemServiceImpl(itemRepository, userRepository, commentRepository,
                bookingRepository, requestRepository, itemMapper);
        Item item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("description");
        item.setAvailable(true);

        Mockito.when(requestRepository.findById(Mockito.anyLong()))
                .thenAnswer(invocation -> {
                    long requestId = invocation.getArgument(0);
                    Request request = new Request();
                    request.setId(requestId);
                    request.setDescription("description_request");
                    request.setCreated(LocalDateTime.of(2025, 1, 3, 19, 15, 0));
                    User user = new User();
                    user.setName("request_owner");
                    user.setEmail("request_owner@mail.com");
                    request.setOwner(user);
                    return Optional.of(request);
                });
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    long userId = invocationOnMock.getArgument(0);
                    User user = new User();
                    user.setId(userId);
                    user.setEmail("user@mail.com");
                    user.setName("user_posting_for_request");
                    return Optional.of(user);
                });
        Mockito.when(itemRepository.save(Mockito.any(Item.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        Item createdItem = itemService.create(item, 1L, 2L);

        assertEquals(1, createdItem.getId());
        assertEquals("item", createdItem.getName());
        assertEquals("description", createdItem.getDescription());
        assertEquals(true, createdItem.getAvailable());
        assertEquals(1, createdItem.getOwner().getId());
        assertEquals("user_posting_for_request", createdItem.getOwner().getName());
        assertEquals("user@mail.com", createdItem.getOwner().getEmail());
        assertEquals(2, createdItem.getRequest().getId());
        assertEquals("description_request", createdItem.getRequest().getDescription());
        assertEquals(LocalDateTime.of(2025, 1, 3, 19, 15, 0),
                createdItem.getRequest().getCreated());
        assertEquals("request_owner", createdItem.getRequest().getOwner().getName());
        assertEquals("request_owner@mail.com", createdItem.getRequest().getOwner().getEmail());
    }
}
