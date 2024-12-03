package com.practice.shareitziyat.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.shareitziyat.user.dto.UserCreateDto;
import com.practice.shareitziyat.user.dto.UserMapper;
import com.practice.shareitziyat.user.dto.UserResponseDto;
import com.practice.shareitziyat.user.dto.UserUpdateDto;
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

import java.util.List;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @SpyBean
    private UserMapper userMapper;

    @Test
    @SneakyThrows
    void findByIdTest() {
        User user = new User();
        user.setId(1L);
        user.setName("User1");
        user.setEmail("email1@email.com");

        Mockito.when(userService.findById(Mockito.anyLong())).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("User1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("email1@email.com"));
    }

    @Test
    @SneakyThrows
    void findAllTest() {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("User1");
        user1.setEmail("email1@email.com");
        User user2 = new User();
        user2.setId(2L);
        user2.setName("User2");
        user2.setEmail("email2@email.com");

        Mockito.when(userService.findAll()).thenReturn(List.of(user1, user2));

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("User1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("email1@email.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("User2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].email").value("email2@email.com"));
    }

    @Test
    @SneakyThrows
    void createTest() {
        UserCreateDto userCreate = new UserCreateDto();
        userCreate.setName("User1");
        userCreate.setEmail("email1@email.com");

        String userJson = objectMapper.writeValueAsString(userCreate);

        Mockito.when(userService.create(Mockito.any(User.class)))
                .thenAnswer(invocationOnMock -> {
                    User user = invocationOnMock.getArgument(0);
                    user.setId(1L);
                    return user;
                });

        mockMvc.perform(MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("User1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("email1@email.com"));
    }

    @Test
    @SneakyThrows
    void updateTest() {
        UserUpdateDto userUpdate = new UserUpdateDto();
        userUpdate.setName("User1");
        userUpdate.setEmail("email1@email.com");

        String userJson = objectMapper.writeValueAsString(userUpdate);

        Mockito.when(userService.update(Mockito.any(User.class), Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    User user = invocationOnMock.getArgument(0);
                    long id = invocationOnMock.getArgument(1);
                    User updatedUser = new User();
                    updatedUser.setId(id);
                    updatedUser.setName(user.getName());
                    updatedUser.setEmail(user.getEmail());
                    return updatedUser;
                });

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/1").contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("User1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("email1@email.com"));
    }
    
    @Test
    @SneakyThrows
    void deleteTest() {
    }
}
