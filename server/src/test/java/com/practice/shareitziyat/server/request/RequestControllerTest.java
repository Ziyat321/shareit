package com.practice.shareitziyat.server.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.shareitziyat.server.comparators.RequestCreatedDateComparator;
import com.practice.shareitziyat.server.item.dto.ItemMapper;
import com.practice.shareitziyat.server.request.dto.RequestCreateDto;
import com.practice.shareitziyat.server.request.dto.RequestMapper;
import com.practice.shareitziyat.server.user.User;
import com.practice.shareitziyat.server.user.dto.UserMapper;
import com.practice.shareitziyat.server.utils.RequestConstants;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;

@WebMvcTest({RequestController.class, ItemMapper.class, UserMapper.class, RequestMapper.class})
public class RequestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RequestService requestService;

    @Test
    @SneakyThrows
    public void createTest() {
        User user = new User();
        user.setId(1L);
        user.setName("user");
        user.setEmail("user@email.com");

        RequestCreateDto requestCreate = new RequestCreateDto();
        requestCreate.setDescription("description");

        String requestJson = objectMapper.writeValueAsString(requestCreate);

        Mockito.when(requestService.create(Mockito.any(Request.class), Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    Request request = invocationOnMock.getArgument(0);
                    request.setId(1L);
                    request.setCreated(LocalDateTime.of(2025, 2, 9, 12, 0, 0));
                    request.setOwner(user);
                    return request;
                });

        mockMvc.perform(MockMvcRequestBuilders.post("/requests")
                        .header(RequestConstants.USER_HEADER, user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created").value("2025-02-09T12:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.owner.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.owner.name").value("user"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.owner.email").value("user@email.com"));
    }

    @Test
    @SneakyThrows
    public void findAllByUserTest() {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("user1");
        user1.setEmail("user1@email.com");
        User user2 = new User();
        user2.setId(2L);
        user2.setName("user2");
        user2.setEmail("user2@email.com");
        Request request1 = new Request();
        request1.setId(1L);
        request1.setDescription("description1");
        request1.setCreated(LocalDateTime.of(2025, 2, 8, 12, 0, 0));
        request1.setOwner(user1);
        Request request2 = new Request();
        request2.setId(2L);
        request2.setDescription("description2");
        request2.setCreated(LocalDateTime.of(2025, 2, 9, 12, 0, 0));
        request2.setOwner(user1);
        Request request3 = new Request();
        request3.setId(3L);
        request3.setCreated(LocalDateTime.of(2025, 2, 9, 12, 0, 0));
        request3.setOwner(user2);
        List<Request> requests = List.of(request1, request2, request3);

        Mockito.when(requestService.findAllByUser(Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    long userId = invocationOnMock.getArgument(0);
                    return requests.stream()
                            .filter(request -> request.getOwner().getId().equals(userId))
                            .sorted((r1, r2) -> {
                                if (r1.getCreated().isAfter(r2.getCreated())) return -1;
                                else if (r1.getCreated().isBefore(r2.getCreated())) return 1;
                                else return 0;
                            }).toList();
                });

        mockMvc.perform(MockMvcRequestBuilders.get("/requests")
                        .header(RequestConstants.USER_HEADER, user1.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description")
                        .value("description2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].created")
                        .value("2025-02-09T12:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].owner.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].owner.name").value("user1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].owner.email")
                        .value("user1@email.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description")
                        .value("description1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].created")
                        .value("2025-02-08T12:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].owner.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].owner.name").value("user1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].owner.email")
                        .value("user1@email.com"));
    }

    @Test
    @SneakyThrows
    public void findAllTest() {
        User user = new User();
        user.setId(1L);
        user.setName("user");
        user.setEmail("user@email.com");
        Request request1 = new Request();
        request1.setId(1L);
        request1.setDescription("description1");
        request1.setCreated(LocalDateTime.of(2025, 2, 8, 12, 0, 0));
        request1.setOwner(user);
        Request request2 = new Request();
        request2.setId(2L);
        request2.setDescription("description2");
        request2.setCreated(LocalDateTime.of(2025, 2, 9, 12, 0, 0));
        request2.setOwner(user);
        List<Request> requests = List.of(request1, request2);

        Mockito.when(requestService.findAll(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(requests.stream()
                        .sorted(new RequestCreatedDateComparator()).toList());

        mockMvc.perform(MockMvcRequestBuilders.get("/requests/all?from=0&size=2")
                        .header(RequestConstants.USER_HEADER, 2L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description")
                        .value("description2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].created")
                        .value("2025-02-09T12:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].owner.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].owner.name").value("user"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].owner.email")
                        .value("user@email.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description")
                        .value("description1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].created")
                        .value("2025-02-08T12:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].owner.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].owner.name").value("user"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].owner.email")
                        .value("user@email.com"));
    }

    @Test
    @SneakyThrows
    public void findByIdTest() {
        User user = new User();
        user.setId(1L);
        user.setName("user");
        user.setEmail("user@email.com");
        Request request1 = new Request();
        request1.setId(1L);
        request1.setDescription("description1");
        request1.setCreated(LocalDateTime.of(2025, 2, 8, 12, 0, 0));
        request1.setOwner(user);

        Mockito.when(requestService.findById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(request1);

        mockMvc.perform(MockMvcRequestBuilders.get("/requests/1")
                        .header(RequestConstants.USER_HEADER, 2L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description")
                        .value("description1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created")
                        .value("2025-02-08T12:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.owner.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.owner.name").value("user"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.owner.email")
                        .value("user@email.com"));
    }
}
