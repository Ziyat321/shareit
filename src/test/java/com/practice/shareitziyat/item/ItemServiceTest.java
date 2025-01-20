package com.practice.shareitziyat.item;

import com.practice.shareitziyat.booking.Booking;
import com.practice.shareitziyat.booking.BookingRepository;
import com.practice.shareitziyat.booking.BookingStatus;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    @Test
    public void updateTest() {
        ItemService itemService = new ItemServiceImpl(itemRepository, userRepository, commentRepository,
                bookingRepository, requestRepository, itemMapper);
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("item1");
        item1.setDescription("description1");
        item1.setAvailable(true);
        User owner = new User();
        owner.setId(1L);
        owner.setName("item_owner1");
        owner.setEmail("item_owner1@mail.com");
        item1.setOwner(owner);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                            long userId = invocationOnMock.getArgument(0);
                            User user = new User();
                            user.setId(userId);
                            user.setName("item_owner2");
                            user.setEmail("item_owner2@mail.com");
                            return Optional.of(user);
                        }
                );
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    long itemId = invocationOnMock.getArgument(0);
                    Item item2 = new Item();
                    item2.setId(itemId);
                    item2.setName("item2");
                    item2.setDescription("description2");
                    item2.setAvailable(true);
                    User user = new User();
                    user.setId(2L);
                    user.setName("item_owner2");
                    user.setEmail("item_owner2@mail.com");
                    item2.setOwner(user);
                    return Optional.of(item2);
                });
        Mockito.when(itemRepository.save(Mockito.any(Item.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        Item updatedItem = itemService.update(item1, 2L, 2L);

        assertEquals(2, updatedItem.getId());
        assertEquals("item1", updatedItem.getName());
        assertEquals("description1", updatedItem.getDescription());
        assertEquals(true, updatedItem.getAvailable());
        assertEquals(1, updatedItem.getOwner().getId());
        assertEquals("item_owner1", updatedItem.getOwner().getName());
        assertEquals("item_owner1@mail.com", updatedItem.getOwner().getEmail());
    }

    @Test
    public void findByIdTest() {
        ItemService itemService = new ItemServiceImpl(itemRepository, userRepository, commentRepository,
                bookingRepository, requestRepository, itemMapper);

        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    long itemId = invocationOnMock.getArgument(0);
                    Item item = new Item();
                    item.setId(itemId);
                    item.setName("item1");
                    item.setDescription("description1");
                    item.setAvailable(true);
                    User user = new User();
                    user.setId(1L);
                    user.setName("item_owner1");
                    user.setEmail("item_owner1@mail.com");
                    item.setOwner(user);
                    return Optional.of(item);
                });

        Item item = itemService.findById(1L);

        assertEquals(1, item.getId());
        assertEquals("item1", item.getName());
        assertEquals("description1", item.getDescription());
        assertEquals(true, item.getAvailable());
        assertEquals(1, item.getOwner().getId());
        assertEquals("item_owner1", item.getOwner().getName());
        assertEquals("item_owner1@mail.com", item.getOwner().getEmail());
    }

    @Test
    public void searchTest() {
        ItemService itemService = new ItemServiceImpl(itemRepository, userRepository, commentRepository,
                bookingRepository, requestRepository, itemMapper);
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("item1");
        item1.setDescription("text1");
        item1.setAvailable(true);
        User user1 = new User();
        user1.setId(1L);
        user1.setName("item_owner1");
        user1.setEmail("item_owner1@mail.com");
        item1.setOwner(user1);
        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("item2");
        item2.setDescription("description2");
        item2.setAvailable(true);
        User user2 = new User();
        user2.setId(2L);
        user2.setName("item_owner2");
        user2.setEmail("item_owner2@mail.com");
        item2.setOwner(user2);
        Item item3 = new Item();
        item3.setId(3L);
        item3.setName("item3");
        item3.setDescription("description3");
        item3.setAvailable(true);
        User user3 = new User();
        user3.setId(3L);
        user3.setName("item_owner3");
        user3.setEmail("item_owner3@mail.com");
        item3.setOwner(user3);
        List<Item> itemList = List.of(item1, item2, item3);

        Mockito.when(itemRepository.search(Mockito.anyString()))
                .thenAnswer(invocationOnMock -> {
                    String keyword = invocationOnMock.getArgument(0);
                    List<Item> itemsFound = new ArrayList<>();
                    for (Item item : itemList) {
                        if (item.getName().contains(keyword) || item.getDescription().contains(keyword)) {
                            itemsFound.add(item);
                        }
                    }
                    return itemsFound;
                });

        List<Item> items = itemService.search("desc");

        assertEquals(2, items.size());
        assertEquals(2, items.get(0).getId());
        assertEquals("item2", items.get(0).getName());
        assertEquals("description2", items.get(0).getDescription());
        assertEquals(true, items.get(0).getAvailable());
        assertEquals(2, items.get(0).getOwner().getId());
        assertEquals("item_owner2", items.get(0).getOwner().getName());
        assertEquals("item_owner2@mail.com", items.get(0).getOwner().getEmail());
        assertEquals(3, items.get(1).getId());
        assertEquals("item3", items.get(1).getName());
        assertEquals("description3", items.get(1).getDescription());
        assertEquals(true, items.get(1).getAvailable());
        assertEquals(3, items.get(1).getOwner().getId());
        assertEquals("item_owner3", items.get(1).getOwner().getName());
        assertEquals("item_owner3@mail.com", items.get(1).getOwner().getEmail());
    }

    @Test
    public void findAllTest() {
        ItemService itemService = new ItemServiceImpl(itemRepository, userRepository, commentRepository,
                bookingRepository, requestRepository, itemMapper);
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("item1");
        item1.setDescription("text1");
        item1.setAvailable(true);
        User user1 = new User();
        user1.setId(1L);
        user1.setName("item_owner1");
        user1.setEmail("item_owner1@mail.com");
        item1.setOwner(user1);
        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("item2");
        item2.setDescription("description2");
        item2.setAvailable(true);
        item2.setOwner(user1);
        Item item3 = new Item();
        item3.setId(3L);
        item3.setName("item3");
        item3.setDescription("description3");
        item3.setAvailable(true);
        User user2 = new User();
        user2.setId(2L);
        user2.setName("item_owner3");
        user2.setEmail("item_owner3@mail.com");
        item3.setOwner(user2);
        List<Item> itemList = List.of(item1, item2, item3);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    long userId = invocationOnMock.getArgument(0);
                    User user = new User();
                    user.setId(userId);
                    user.setName("item_owner1");
                    user.setEmail("item_owner1@mail.com");
                    return Optional.of(user);
                });
        Mockito.when(itemRepository.findAllByOwner_Id(Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    long owmerId = invocationOnMock.getArgument(0);
                    List<Item> itemsFound = new ArrayList<>();
                    for (Item item : itemList) {
                        if (item.getOwner().getId() == owmerId) {
                            itemsFound.add(item);
                        }
                    }
                    return itemsFound;
                });

        List<Item> items = itemService.findAll(1L);

        assertEquals(2, items.size());
        assertEquals(1, items.get(0).getId());
        assertEquals("item1", items.get(0).getName());
        assertEquals("text1", items.get(0).getDescription());
        assertEquals(true, items.get(0).getAvailable());
        assertEquals(1, items.get(0).getOwner().getId());
        assertEquals("item_owner1", items.get(0).getOwner().getName());
        assertEquals("item_owner1@mail.com", items.get(0).getOwner().getEmail());
        assertEquals(2, items.get(1).getId());
        assertEquals("item2", items.get(1).getName());
        assertEquals("description2", items.get(1).getDescription());
        assertEquals(true, items.get(1).getAvailable());
        assertEquals(1, items.get(1).getOwner().getId());
        assertEquals("item_owner1", items.get(1).getOwner().getName());
        assertEquals("item_owner1@mail.com", items.get(1).getOwner().getEmail());
    }

    @Test
    public void createCommentTest() {
        ItemService itemService = new ItemServiceImpl(itemRepository, userRepository, commentRepository,
                bookingRepository, requestRepository, itemMapper);
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("comment");

        Mockito.when(bookingRepository.findByUser_IdAndItem_IdAndStatusIsAndStartDateBefore(
                Mockito.anyLong(), Mockito.anyLong(), Mockito.any(BookingStatus.class), Mockito.any(LocalDateTime.class)
        )).thenAnswer(
                invocationOnMock -> {
                    long userId = invocationOnMock.getArgument(0);
                    User user1 = new User();
                    user1.setId(userId);
                    user1.setName("user1");
                    user1.setEmail("user1@mail.com");
                    long itemId = invocationOnMock.getArgument(1);
                    Item item = new Item();
                    item.setId(itemId);
                    item.setName("item1");
                    item.setDescription("text1");
                    item.setAvailable(true);
                    User user2 = new User();
                    user2.setId(userId + 1);
                    user2.setName("user2");
                    user2.setEmail("user2@mail.com");
                    item.setOwner(user2);
                    Booking booking = new Booking();
                    booking.setId(1L);
                    booking.setStartDate(LocalDateTime.of(2025, 1, 2, 12, 0, 0));
                    booking.setEndDate(LocalDateTime.of(2025, 5, 1, 12, 0, 0));
                    booking.setStatus(BookingStatus.APPROVED);
                    booking.setUser(user1);
                    booking.setItem(item);
                    return List.of(booking);
                }
        );
        Mockito.when(commentRepository.save(Mockito.any(Comment.class))).
                thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0)
        );

        Comment commentSaved = itemService.createComment(comment, 1L, 1L);

        assertEquals(1, commentSaved.getId());
        assertEquals("comment", commentSaved.getText());
        assertEquals(1, commentSaved.getItem().getId());
        assertEquals("item1", commentSaved.getItem().getName());
        assertEquals("text1", commentSaved.getItem().getDescription());
        assertEquals(2, commentSaved.getItem().getOwner().getId());
        assertEquals("user2", commentSaved.getItem().getOwner().getName());
        assertEquals("user2@mail.com", commentSaved.getItem().getOwner().getEmail());
        assertEquals(1, commentSaved.getUser().getId());
        assertEquals("user1", commentSaved.getUser().getName());
        assertEquals("user1@mail.com", commentSaved.getUser().getEmail());
    }

    @Test
    public void findCommentsByUserTest() {
        ItemService itemService = new ItemServiceImpl(itemRepository, userRepository, commentRepository,
                bookingRepository, requestRepository, itemMapper);
        User user1 = new User();
        user1.setId(1L);
        user1.setName("user1");
        user1.setEmail("user1@mail.com");
        User user2 = new User();
        user2.setId(2L);
        user2.setName("user2");
        user2.setEmail("user2@mail.com");
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("item1");
        item1.setDescription("text1");
        item1.setAvailable(true);
        User owner = new User();
        owner.setId(3L);
        owner.setName("user3");
        owner.setEmail("user3@mail.com");
        item1.setOwner(owner);
        Comment comment1 = new Comment();
        comment1.setId(1L);
        comment1.setText("comment1");
        comment1.setCreated(LocalDateTime.of(2025, 1, 2, 12, 0, 0));
        comment1.setUser(user1);
        comment1.setItem(item1);
        Comment comment2 = new Comment();
        comment2.setId(2L);
        comment2.setText("comment2");
        comment2.setCreated(LocalDateTime.of(2025, 2, 1, 12, 0, 0));
        comment2.setUser(user2);
        comment2.setItem(item1);
        Comment comment3 = new Comment();
        comment3.setId(3L);
        comment3.setText("comment3");
        comment3.setCreated(LocalDateTime.of(2025, 3, 1, 12, 0, 0));
        comment3.setUser(user1);
        comment3.setItem(item1);
        List<Comment> commentList = List.of(comment1, comment2, comment3);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                        .thenAnswer(invocationOnMock -> {
                            long userId = invocationOnMock.getArgument(0);
                            User user = new User();
                            user.setId(userId);
                            user.setName("user1");
                            user.setEmail("user1@mail.com");
                            return Optional.of(user);
                        });
        Mockito.when(commentRepository.findAllByUser_Id(Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    long userId = invocationOnMock.getArgument(0);
                    List<Comment> commentsFound = new ArrayList<>();
                    for (Comment comment : commentList) {
                        if (comment.getUser().getId() == userId) {
                            commentsFound.add(comment);
                        }
                    }
                    return commentsFound;
                });

        List<Comment> comments = itemService.findCommentsByUser(1L);

        assertEquals(2, comments.size());
        assertEquals(1, comments.get(0).getId());
        assertEquals("comment1", comments.get(0).getText());
        assertEquals(LocalDateTime.of(2025, 1, 2, 12, 0, 0), comments.get(0).getCreated());
        assertEquals(1, comments.get(0).getUser().getId());
        assertEquals("user1", comments.get(0).getUser().getName());
        assertEquals("user1@mail.com", comments.get(0).getUser().getEmail());
        assertEquals(1, comments.get(0).getItem().getId());
        assertEquals("item1", comments.get(0).getItem().getName());
        assertEquals("text1", comments.get(0).getItem().getDescription());
        assertEquals(true, comments.get(0).getItem().getAvailable());
        assertEquals(3, comments.get(0).getItem().getOwner().getId());
        assertEquals("user3", comments.get(0).getItem().getOwner().getName());
        assertEquals("user3@mail.com", comments.get(0).getItem().getOwner().getEmail());
        assertEquals(3, comments.get(1).getId());
        assertEquals("comment3", comments.get(1).getText());
        assertEquals(LocalDateTime.of(2025, 3, 1, 12, 0, 0), comments.get(1).getCreated());
        assertEquals(1, comments.get(1).getUser().getId());
        assertEquals("user1", comments.get(1).getUser().getName());
        assertEquals("user1@mail.com", comments.get(1).getUser().getEmail());
        assertEquals(1, comments.get(1).getItem().getId());
        assertEquals("item1", comments.get(1).getItem().getName());
        assertEquals("text1", comments.get(1).getItem().getDescription());
        assertEquals(true, comments.get(1).getItem().getAvailable());
        assertEquals(3, comments.get(1).getItem().getOwner().getId());
        assertEquals("user3", comments.get(1).getItem().getOwner().getName());
        assertEquals("user3@mail.com", comments.get(1).getItem().getOwner().getEmail());
    }

    @Test
    public void findCommentsByItemTest() {
        ItemService itemService = new ItemServiceImpl(itemRepository, userRepository, commentRepository,
                bookingRepository, requestRepository, itemMapper);
        User owner = new User();
        owner.setId(1L);
        owner.setName("user1");
        owner.setEmail("user1@mail.com");
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("item1");
        item1.setDescription("text1");
        item1.setAvailable(true);
        item1.setOwner(owner);
        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("item2");
        item2.setDescription("text2");
        item2.setAvailable(true);
        item2.setOwner(owner);
        User user = new User();
        user.setId(2L);
        user.setName("user2");
        user.setEmail("user2@mail.com");
        Comment comment1 = new Comment();
        comment1.setId(1L);
        comment1.setText("comment1");
        comment1.setCreated(LocalDateTime.of(2025, 1, 2, 12, 0, 0));
        comment1.setUser(user);
        comment1.setItem(item1);
        Comment comment2 = new Comment();
        comment2.setId(2L);
        comment2.setText("comment2");
        comment2.setCreated(LocalDateTime.of(2025, 2, 1, 12, 0, 0));
        comment2.setUser(user);
        comment2.setItem(item1);
        Comment comment3 = new Comment();
        comment3.setId(3L);
        comment3.setText("comment3");
        comment3.setCreated(LocalDateTime.of(2025, 3, 1, 12, 0, 0));
        comment3.setUser(user);
        comment3.setItem(item2);
        List<Comment> commentList = List.of(comment1, comment2, comment3);

        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    long itemId = invocationOnMock.getArgument(0);
                    Item item = new Item();
                    item.setId(itemId);
                    item.setName("item1");
                    item.setDescription("text1");
                    item.setAvailable(true);
                    item.setOwner(owner);
                    return Optional.of(item);
                });
        Mockito.when(commentRepository.findAllByItem_Id(Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    long itemId = invocationOnMock.getArgument(0);
                    List<Comment> commentsFound = new ArrayList<>();
                    for (Comment comment : commentList) {
                        if (comment.getItem().getId() == itemId) {
                            commentsFound.add(comment);
                        }
                    }
                    return commentsFound;
                });

        List<Comment> comments = itemService.findCommentsByItem(1L);
        assertEquals(2, comments.size());
        assertEquals(1, comments.get(0).getId());
        assertEquals("comment1", comments.get(0).getText());
        assertEquals(LocalDateTime.of(2025, 1, 2, 12, 0, 0), comments.get(0).getCreated());
        assertEquals(2, comments.get(0).getUser().getId());
        assertEquals("user2", comments.get(0).getUser().getName());
        assertEquals("user2@mail.com", comments.get(0).getUser().getEmail());
        assertEquals(1, comments.get(0).getItem().getId());
        assertEquals("item1", comments.get(0).getItem().getName());
        assertEquals("text1", comments.get(0).getItem().getDescription());
        assertEquals(true, comments.get(0).getItem().getAvailable());
        assertEquals(1, comments.get(0).getItem().getOwner().getId());
        assertEquals("user1", comments.get(0).getItem().getOwner().getName());
        assertEquals("user1@mail.com", comments.get(0).getItem().getOwner().getEmail());
        assertEquals(2, comments.get(1).getId());
        assertEquals("comment2", comments.get(1).getText());
        assertEquals(LocalDateTime.of(2025, 2, 1, 12, 0, 0), comments.get(1).getCreated());
        assertEquals(2, comments.get(1).getUser().getId());
        assertEquals("user2", comments.get(1).getUser().getName());
        assertEquals("user2@mail.com", comments.get(1).getUser().getEmail());
        assertEquals(1, comments.get(1).getItem().getId());
        assertEquals("item1", comments.get(1).getItem().getName());
        assertEquals("text1", comments.get(1).getItem().getDescription());
        assertEquals(true, comments.get(1).getItem().getAvailable());
        assertEquals(1, comments.get(1).getItem().getOwner().getId());
        assertEquals("user1", comments.get(1).getItem().getOwner().getName());
        assertEquals("user1@mail.com", comments.get(1).getItem().getOwner().getEmail());
    }
}
