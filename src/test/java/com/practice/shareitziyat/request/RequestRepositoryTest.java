package com.practice.shareitziyat.request;

import com.practice.shareitziyat.user.User;
import com.practice.shareitziyat.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
public class RequestRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Test
    public void findAllByOwnerOrderByCreatedDescTest() throws InterruptedException {
        User owner = new User();
        owner.setName("user");
        owner.setEmail("user@mail.com");
        User savedOwner = userRepository.save(owner);

        Request request1 = new Request();
        request1.setDescription("description1");
        request1.setOwner(savedOwner);
        requestRepository.save(request1);
        Thread.sleep(1000);
        Request request2 = new Request();
        request2.setDescription("description2");
        request2.setOwner(savedOwner);
        requestRepository.save(request2);

        List<Request> requests = requestRepository.findAllByOwnerOrderByCreatedDesc(savedOwner);

        assertEquals(2, requests.size());
        assertEquals("description2", requests.get(0).getDescription());
        assertEquals("user", requests.get(0).getOwner().getName());
        assertEquals("user@mail.com", requests.get(0).getOwner().getEmail());
        assertEquals("description1", requests.get(1).getDescription());
        assertEquals("user", requests.get(1).getOwner().getName());
        assertEquals("user@mail.com", requests.get(1).getOwner().getEmail());
    }
}
