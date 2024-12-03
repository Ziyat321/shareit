package com.practice.shareitziyat.user;

import com.practice.shareitziyat.exceptions.NotFoundException;
import com.practice.shareitziyat.exceptions.UserAlreadyExistsException;
import com.practice.shareitziyat.user.dto.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @Spy
    UserMapper userMapper;

    UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, userMapper);
    }

    @Test
    void findByIdTest() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> userService.findById(1L));

        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void findByIdTest2() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@mail.com");
        user.setName("user");

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        User byId = userService.findById(1L);
        assertEquals(user.getId(), byId.getId());
        assertEquals(user.getEmail(), byId.getEmail());
        assertEquals(user.getName(), byId.getName());
    }

    @Test
    void createExceptionTest() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@mail.com");
        user.setName("user");

        Mockito.when(userRepository.findByEmail(Mockito.anyString()))
                .thenAnswer(invocation -> {
                    User user2 = new User();
                    user2.setId(2L);
                    user2.setEmail("user2@mail.com");
                    user2.setName("user2");
                    return Optional.of(user2);
                });

        UserAlreadyExistsException ex = assertThrows(UserAlreadyExistsException.class,
                () -> userService.create(user));

        assertEquals("Пользователь с email:" + user.getEmail() + "уже существует", ex.getMessage());
    }

    @Test
    void createTest() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@mail.com");
        user.setName("user");

        Mockito.when(userRepository.findByEmail(Mockito.anyString()))
                .thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(user);

        User created = userService.create(user);
        assertEquals(user.getId(), created.getId());
        assertEquals(user.getName(), created.getName());
        assertEquals(user.getEmail(), created.getEmail());
    }

    @Test
    void updateTest() {
        User user = new User();
        user.setEmail("user@mail.com");
        user.setName("user");

        Mockito.when(userRepository.findByEmail(Mockito.anyString()))
                .thenReturn(Optional.of(user));
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    long userId = invocationOnMock.getArgument(0);
                    User user2 = new User();
                    user2.setId(userId);
                    user2.setEmail("user2@mail.com");
                    user2.setName("user2");
                    return Optional.of(user2);
                });
        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        User updated = userService.update(user, 1L);
        assertEquals(user.getId(), updated.getId());
        assertEquals(user.getName(), updated.getName());
        assertEquals(user.getEmail(), updated.getEmail());
    }

    @Test
    void findAllTest() {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("User1");
        user1.setEmail("email1@email.com");
        User user2 = new User();
        user2.setId(2L);
        user2.setName("User2");
        user2.setEmail("email2@email.com");

        Mockito.when(userRepository.findAll())
                .thenReturn(List.of(user1, user2));

        List<User> users = userService.findAll();
        assertEquals(2, users.size());
        assertEquals(user1.getId(), users.get(0).getId());
        assertEquals(user2.getId(), users.get(1).getId());
        assertEquals(user1.getName(), users.get(0).getName());
        assertEquals(user2.getName(), users.get(1).getName());
        assertEquals(user1.getEmail(), users.get(0).getEmail());
        assertEquals(user2.getEmail(), users.get(1).getEmail());
    }
}
