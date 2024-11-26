package com.practice.shareitziyat.user;

import com.practice.shareitziyat.exceptions.NotFoundException;
import com.practice.shareitziyat.user.dto.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @Spy
    UserMapper userMapper;


    @Test
    void findByIdTest() {
        UserService userService = new UserServiceImpl(userRepository, userMapper);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> userService.findById(1L));

        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void findByIdTest2() {
        UserService userService = new UserServiceImpl(userRepository, userMapper);
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
}
