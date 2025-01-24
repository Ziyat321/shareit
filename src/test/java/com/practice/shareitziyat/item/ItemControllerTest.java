package com.practice.shareitziyat.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.shareitziyat.item.dto.CommentCreateDto;
import com.practice.shareitziyat.item.dto.ItemCreateDto;
import com.practice.shareitziyat.item.dto.ItemMapper;
import com.practice.shareitziyat.item.dto.ItemUpdateDto;
import com.practice.shareitziyat.user.User;
import com.practice.shareitziyat.utils.RequestConstants;
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
                        .header(RequestConstants.USER_HEADER, user.getId())
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
                        .header(RequestConstants.USER_HEADER, user.getId())
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

        mockMvc.perform(MockMvcRequestBuilders.get("/items")
                        .header(RequestConstants.USER_HEADER, owner.getId()))
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

        Mockito.when(itemService.findById(Mockito.anyLong()))
                .thenReturn(item);

        mockMvc.perform(MockMvcRequestBuilders.get("/items/1"))
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
                .header(RequestConstants.USER_HEADER, user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonComment))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value("comment"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorName").value("user"));
//                .andExpect(MockMvcResultMatchers.jsonPath("$.created").value(
//                        LocalDateTime.of(2025, 1,24, 12, 10, 30)));
    }
}
