package com.practice.shareitziyat.item;

import com.practice.shareitziyat.booking.BookingRepository;
import com.practice.shareitziyat.user.User;
import com.practice.shareitziyat.user.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
public class ItemRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @BeforeEach
    public void cleanUp() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void findAllByOwnerIdTest() {
        User user = new User();
        user.setName("User1");
        user.setEmail("email1@email.com");
        User savedUser = userRepository.save(user);

        Item item1 = new Item();
        item1.setName("Item1");
        item1.setDescription("Description1");
        item1.setAvailable(true);
        item1.setOwner(savedUser);
        itemRepository.save(item1);
        Item item2 = new Item();
        item2.setName("Item2");
        item2.setDescription("Description2");
        item2.setAvailable(true);
        item2.setOwner(savedUser);
        itemRepository.save(item2);

        List<Item> items = itemRepository.findAllByOwner_Id(savedUser.getId());

        assertEquals(2, items.size());
        assertEquals("Item1", items.get(0).getName());
        assertEquals("Description1", items.get(0).getDescription());
        assertTrue(items.get(0).getAvailable());
        assertEquals("User1", items.get(0).getOwner().getName());
        assertEquals("email1@email.com", items.get(0).getOwner().getEmail());
        assertEquals("Item2", items.get(1).getName());
        assertEquals("Description2", items.get(1).getDescription());
        assertTrue(items.get(1).getAvailable());
        assertEquals("User1", items.get(1).getOwner().getName());
        assertEquals("email1@email.com", items.get(1).getOwner().getEmail());
    }

    @Test
    public void searchTest() {
        User user = new User();
        user.setName("User1");
        user.setEmail("email1@email.com");
        User savedUser = userRepository.save(user);

        Item item1 = new Item();
        item1.setName("Item1");
        item1.setDescription("Description1");
        item1.setAvailable(true);
        item1.setOwner(savedUser);
        itemRepository.save(item1);
        Item item2 = new Item();
        item2.setName("Item2");
        item2.setDescription("Description2");
        item2.setAvailable(true);
        item2.setOwner(savedUser);
        itemRepository.save(item2);

        List<Item> items = itemRepository.search("desc");

        assertEquals(2, items.size());
        assertEquals("Item1", items.get(0).getName());
        assertEquals("Description1", items.get(0).getDescription());
        assertTrue(items.get(0).getAvailable());
        assertEquals("User1", items.get(0).getOwner().getName());
        assertEquals("email1@email.com", items.get(0).getOwner().getEmail());
        assertEquals("Item2", items.get(1).getName());
        assertEquals("Description2", items.get(1).getDescription());
        assertTrue(items.get(1).getAvailable());
        assertEquals("User1", items.get(1).getOwner().getName());
        assertEquals("email1@email.com", items.get(1).getOwner().getEmail());
    }
}
