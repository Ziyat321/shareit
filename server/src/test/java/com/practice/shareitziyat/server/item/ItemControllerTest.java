package com.practice.shareitziyat.server.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.shareitziyat.server.item.dto.CommentCreateDto;
import com.practice.shareitziyat.server.item.dto.ItemCreateDto;
import com.practice.shareitziyat.server.item.dto.ItemMapper;
import com.practice.shareitziyat.server.item.dto.ItemUpdateDto;
import com.practice.shareitziyat.server.user.User;
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

import static com.practice.shareitziyat.server.utils.RequestConstants.USER_HEADER;

@WebMvcTest(ItemController.class)
public class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    @SpyBean
    private ItemMapper itemMapper;

    @Test
    @SneakyThrows
    void createTest() {
        User user = new User();
        user.setId(1L);
        user.setName("user");
        user.setEmail("user@email.com");

        ItemCreateDto itemCreate = new ItemCreateDto();
        itemCreate.setName("item");
        itemCreate.setDescription("description");
        itemCreate.setAvailable(true);

        String itemJson = objectMapper.writeValueAsString(itemCreate);

        Mockito.when(itemService.create(Mockito.any(Item.class), Mockito.anyLong(), Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    Item item = invocationOnMock.getArgument(0);
                    item.setId(1L);
                    item.setOwner(user);
                    return item;
                });

        mockMvc.perform(MockMvcRequestBuilders.post("/items?requestId=1")
                        .header(USER_HEADER, user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("item"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.available").value(true));
    }

    @Test
    @SneakyThrows
    void updateTest() {
        User user = new User();
        user.setId(1L);
        user.setName("user");
        user.setEmail("user@email.com");

        ItemUpdateDto itemUpdate = new ItemUpdateDto();
        itemUpdate.setName("item");
        itemUpdate.setDescription("description");
        itemUpdate.setAvailable(true);

        String itemJson = objectMapper.writeValueAsString(itemUpdate);

        Mockito.when(itemService.update(Mockito.any(Item.class), Mockito.anyLong(), Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    Item item = invocationOnMock.getArgument(0);
                    long itemId = invocationOnMock.getArgument(1);
                    Item updatedItem = new Item();
                    updatedItem.setId(itemId);
                    updatedItem.setName(item.getName());
                    updatedItem.setDescription(item.getDescription());
                    updatedItem.setAvailable(item.getAvailable());
                    updatedItem.setOwner(user);
                    return updatedItem;
                });

        mockMvc.perform(MockMvcRequestBuilders.patch("/items/2")
                        .header(USER_HEADER, user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("item"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.available").value(true));
    }

    @Test
    @SneakyThrows
    void findAllTest() {
        User owner = new User();
        owner.setId(1L);
        owner.setName("user");
        owner.setEmail("user@mail.com");

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

        Mockito.when(itemService.findAll(Mockito.any()))
                .thenReturn(List.of(item1, item2));

        mockMvc.perform(MockMvcRequestBuilders.delete("/items/3"));
        mockMvc.perform(MockMvcRequestBuilders.get("/items")
                        .header(USER_HEADER, owner.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("item1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("description1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].available").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("item2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value("description2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].available").value(true));
    }

    @Test
    @SneakyThrows
    void findByIdTest() {
        Item item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("description");
        item.setAvailable(true);

        Mockito.when(itemService.findById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(item);

        mockMvc.perform(MockMvcRequestBuilders.get("/items/1")
                        .header(USER_HEADER, "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("item"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.available").value(true));
    }

    @Test
    @SneakyThrows
    void searchTest() {
        User owner = new User();
        owner.setId(1L);
        owner.setName("user");
        owner.setEmail("user@mail.com");

        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("item1");
        item1.setDescription("description1");
        item1.setAvailable(true);
        item1.setOwner(owner);
        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("item2");
        item2.setDescription("text2");
        item2.setAvailable(true);
        item2.setOwner(owner);
        List<Item> itemList = List.of(item1, item2);

        Mockito.when(itemService.search(Mockito.anyString()))
                .thenAnswer(invocationOnMock -> {
                    String keyWord = invocationOnMock.getArgument(0);
                    return itemList.stream()
                            .filter(item -> item.getName().toLowerCase().contains(keyWord.toLowerCase())
                                    || item.getDescription().toLowerCase().contains(keyWord.toLowerCase()))
                            .toList();
                });

        mockMvc.perform(MockMvcRequestBuilders.get("/items/search?text=desc"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("item1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("description1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].available").value(true));
    }

    @Test
    @SneakyThrows
    void createCommentTest() {
        User user = new User();
        user.setId(1L);
        user.setName("user");
        user.setEmail("user@mail.com");

        Item item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("description");
        item.setAvailable(true);


        CommentCreateDto commentCreate = new CommentCreateDto();
        commentCreate.setText("comment");

        String jsonComment = objectMapper.writeValueAsString(commentCreate);

        Mockito.when(itemService.createComment(Mockito.any(Comment.class), Mockito.anyLong(), Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    Comment comment = invocationOnMock.getArgument(0);
                    comment.setId(1L);
                    comment.setCreated(
                            LocalDateTime.of(2025, 1,24, 12, 10,30));
                    comment.setItem(item);
                    comment.setUser(user);
                    return comment;
                });

        mockMvc.perform(MockMvcRequestBuilders.post("/items/1/comment")
                .header(USER_HEADER, user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonComment))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value("comment"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorName").value("user"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created").value("2025-01-24T12:10:30"));
    }

    @Test
    @SneakyThrows
    void findCommentsByUserTest() {
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
        item1.setDescription("description1");
        item1.setAvailable(true);
        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("item2");
        item2.setDescription("description2");
        item2.setAvailable(true);

        Comment comment1 = new Comment();
        comment1.setId(1L);
        comment1.setText("comment1");
        comment1.setCreated(LocalDateTime.of(2025, 1, 24, 12, 10,30));
        comment1.setItem(item1);
        comment1.setUser(user1);
        Comment comment2 = new Comment();
        comment2.setId(2L);
        comment2.setText("comment2");
        comment2.setCreated(LocalDateTime.of(2025, 1, 24, 12, 10,30));
        comment2.setItem(item1);
        comment2.setUser(user2);
        Comment comment3 = new Comment();
        comment3.setId(3L);
        comment3.setText("comment3");
        comment3.setCreated(LocalDateTime.of(2025, 1, 24, 12, 10,30));
        comment3.setItem(item2);
        comment3.setUser(user1);
        List<Comment> commentList = List.of(comment1, comment2, comment3);

        Mockito.when(itemService.findCommentsByUser(Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                   long userId = invocationOnMock.getArgument(0);
                   return commentList.stream()
                           .filter(comment -> comment.getUser().getId() == userId)
                           .toList();
                });

        mockMvc.perform(MockMvcRequestBuilders.get("/items/comments/users/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].text").value("comment1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].authorName").value("user1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].created").value("2025-01-24T12:10:30"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].text").value("comment3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].authorName").value("user1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].created").value("2025-01-24T12:10:30"));
    }

    @Test
    @SneakyThrows
    void findCommentsByItemTest() {
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
        item1.setDescription("description1");
        item1.setAvailable(true);
        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("item2");
        item2.setDescription("description2");
        item2.setAvailable(true);

        Comment comment1 = new Comment();
        comment1.setId(1L);
        comment1.setText("comment1");
        comment1.setCreated(LocalDateTime.of(2025, 1, 24, 12, 10,30));
        comment1.setItem(item1);
        comment1.setUser(user1);
        Comment comment2 = new Comment();
        comment2.setId(2L);
        comment2.setText("comment2");
        comment2.setCreated(LocalDateTime.of(2025, 1, 24, 12, 10,30));
        comment2.setItem(item1);
        comment2.setUser(user2);
        Comment comment3 = new Comment();
        comment3.setId(3L);
        comment3.setText("comment3");
        comment3.setCreated(LocalDateTime.of(2025, 1, 24, 12, 10,30));
        comment3.setItem(item2);
        comment3.setUser(user1);
        List<Comment> commentList = List.of(comment1, comment2, comment3);

        Mockito.when(itemService.findCommentsByItem(Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                   long itemId = invocationOnMock.getArgument(0);
                   return commentList.stream()
                           .filter(comment -> comment.getItem().getId() == itemId)
                           .toList();
                });

        mockMvc.perform(MockMvcRequestBuilders.get("/items/comments/items/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].text").value("comment1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].authorName").value("user1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].created").value("2025-01-24T12:10:30"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].text").value("comment2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].authorName").value("user2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].created").value("2025-01-24T12:10:30"));
    }
}
