package com.practice.shareitziyat.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    public void findByIdTest() {
        User user = new User();
        user.setId(1L);
        user.setName("User1");
        user.setEmail("email1@email.com");
        userRepository.save(user);

        Optional<User> userOptional = userRepository.findById(1L);

        assertTrue(userOptional.isPresent());
        assertEquals(user.getId(), userOptional.get().getId());
        assertEquals("User1", userOptional.get().getName());
        assertEquals("email1@email.com", userOptional.get().getEmail());
    }

    @Test
    public void findAllTest() {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("User1");
        user1.setEmail("email1@email.com");
        userRepository.save(user1);
        User user2 = new User();
        user2.setId(2L);
        user2.setName("User2");
        user2.setEmail("email2@email.com");
        userRepository.save(user2);

        List<User> users = userRepository.findAll();

        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
        assertEquals( user1.getId(), users.get(0).getId());
        assertEquals( user2.getId(), users.get(1).getId());
        assertEquals( user1.getName(), users.get(0).getName());
        assertEquals( user2.getName(), users.get(1).getName());
        assertEquals(user1.getEmail(), users.get(0).getEmail());
        assertEquals(user2.getEmail(), users.get(1).getEmail());
    }

    @Test
    public void findByEmailTest() {
        User user = new User();
        user.setId(1L);
        user.setName("User1");
        user.setEmail("email1@email.com");
        userRepository.save(user);

        Optional<User> userOptional = userRepository.findByEmail("email1@email.com");

        assertTrue(userOptional.isPresent());
        assertEquals(user.getId(), userOptional.get().getId());
        assertEquals("User1", userOptional.get().getName());
        assertEquals("email1@email.com", userOptional.get().getEmail());
    }

    @Test
    public void deleteByIdTest() {
        User user = new User();
        user.setId(1L);
        user.setName("User1");
        user.setEmail("email1@email.com");
        userRepository.save(user);

        userRepository.deleteById(1L);

        List<User> users = userRepository.findAll();
        assertEquals(0, users.size());
    }
}
