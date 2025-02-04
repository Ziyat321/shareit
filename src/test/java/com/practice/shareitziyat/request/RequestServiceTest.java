package com.practice.shareitziyat.request;

import com.practice.shareitziyat.item.dto.ItemMapper;
import com.practice.shareitziyat.request.dto.RequestMapper;
import com.practice.shareitziyat.user.User;
import com.practice.shareitziyat.user.UserRepository;
import com.practice.shareitziyat.user.dto.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {
    @Mock
    RequestRepository requestRepository;

    @Mock
    UserRepository userRepository;

    @Spy
    ItemMapper itemMapper;

    @Spy
    UserMapper userMapper;

    @Spy
    RequestMapper requestMapper = new RequestMapper(itemMapper, userMapper);

    @Test
    public void createTest() {
        RequestService requestService = new RequestServiceImpl(requestRepository, userRepository, requestMapper);
        Request request = new Request();
        request.setId(1L);
        request.setDescription("description");
        request.setCreated(LocalDateTime.of(2025, 2, 4, 12, 0, 0));

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    long userId = invocationOnMock.getArgument(0);
                    User user = new User();
                    user.setId(userId);
                    user.setName("user");
                    user.setEmail("user@mail.com");
                    return Optional.of(user);
                });
        Mockito.when(requestRepository.save(Mockito.any(Request.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        Request createdRequest = requestService.create(request, 1L);

        assertEquals(1, createdRequest.getId());
        assertEquals("description", createdRequest.getDescription());
        assertEquals(LocalDateTime.of(2025, 2, 4, 12, 0, 0),
                createdRequest.getCreated());
        assertEquals(1, createdRequest.getOwner().getId());
        assertEquals("user", createdRequest.getOwner().getName());
        assertEquals("user@mail.com", createdRequest.getOwner().getEmail());
    }

    @Test
    public void findAllByUserTest() {
        RequestService requestService = new RequestServiceImpl(requestRepository, userRepository, requestMapper);
        User user = new User();
        user.setId(1L);
        user.setName("user");
        user.setEmail("user@mail.com");
        Request request1 = new Request();
        request1.setId(1L);
        request1.setDescription("description1");
        request1.setCreated(LocalDateTime.of(2025, 1, 8, 12, 0, 0));
        request1.setOwner(user);
        Request request2 = new Request();
        request2.setId(2L);
        request2.setDescription("description2");
        request2.setCreated(LocalDateTime.of(2025, 2, 4, 12, 0, 0));
        request2.setOwner(user);
        List<Request> requests = List.of(request1, request2);

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(requestRepository.findAllByOwnerOrderByCreatedDesc(Mockito.any(User.class)))
                .thenReturn(requests.stream()
                        .sorted((r1, r2) -> {
                            if (r1.getCreated().isAfter(r2.getCreated())) return -1;
                            else if (r1.getCreated().isBefore(r2.getCreated())) return 1;
                            else return 0;
                        }).toList());

        List<Request> requestList = requestService.findAllByUser(1L);

        assertEquals(2, requestList.size());
        assertEquals(2, requestList.get(0).getId());
        assertEquals("description2", requestList.get(0).getDescription());
        assertEquals(LocalDateTime.of(2025, 2, 4, 12, 0, 0),
                requestList.get(0).getCreated());
        assertEquals(1, requestList.get(0).getOwner().getId());
        assertEquals("user", requestList.get(0).getOwner().getName());
        assertEquals("user@mail.com", requestList.get(0).getOwner().getEmail());
        assertEquals(1, requestList.get(1).getId());
        assertEquals("description1", requestList.get(1).getDescription());
        assertEquals(LocalDateTime.of(2025, 1, 8, 12, 0, 0),
                requestList.get(1).getCreated());
        assertEquals(1, requestList.get(1).getOwner().getId());
        assertEquals("user", requestList.get(1).getOwner().getName());
        assertEquals("user@mail.com", requestList.get(1).getOwner().getEmail());
    }

    @Test
    public void findAllTest() {
        RequestService requestService = new RequestServiceImpl(requestRepository, userRepository, requestMapper);
        User user = new User();
        user.setId(1L);
        user.setName("user");
        user.setEmail("user@mail.com");
        Request request1 = new Request();
        request1.setId(1L);
        request1.setDescription("description1");
        request1.setCreated(LocalDateTime.of(2025, 1, 8, 12, 0, 0));
        request1.setOwner(user);
        Request request2 = new Request();
        request2.setId(2L);
        request2.setDescription("description2");
        request2.setCreated(LocalDateTime.of(2025, 2, 4, 12, 0, 0));
        request2.setOwner(user);
        List<Request> requests = List.of(request1, request2);

        Mockito.when(requestRepository.findAll(Mockito.any(PageRequest.class)))
                .thenAnswer(invocationOnMock -> {
                    List<Request> requests1 = (requests.stream()
                            .sorted((r1, r2) -> {
                                if (r1.getCreated().isAfter(r2.getCreated())) return -1;
                                else if (r1.getCreated().isBefore(r2.getCreated())) return 1;
                                else return 0;
                            }).toList());
                    return new PageImpl<>(requests1);
                });

        List<Request> requestList = requestService.findAll(0,2);

        assertEquals(2, requestList.size());
        assertEquals(2, requestList.get(0).getId());
        assertEquals("description2", requestList.get(0).getDescription());
        assertEquals(LocalDateTime.of(2025, 2, 4, 12, 0, 0),
                requestList.get(0).getCreated());
        assertEquals(1, requestList.get(0).getOwner().getId());
        assertEquals("user", requestList.get(0).getOwner().getName());
        assertEquals("user@mail.com", requestList.get(0).getOwner().getEmail());
        assertEquals(1, requestList.get(1).getId());
        assertEquals("description1", requestList.get(1).getDescription());
        assertEquals(LocalDateTime.of(2025, 1, 8, 12, 0, 0),
                requestList.get(1).getCreated());
        assertEquals(1, requestList.get(1).getOwner().getId());
        assertEquals("user", requestList.get(1).getOwner().getName());
        assertEquals("user@mail.com", requestList.get(1).getOwner().getEmail());
    }

    @Test
    public void findByIdTest() {
        RequestService requestService = new RequestServiceImpl(requestRepository, userRepository, requestMapper);
        User user = new User();
        user.setId(1L);
        user.setName("user");
        user.setEmail("user@mail.com");
        Request request = new Request();
        request.setId(1L);
        request.setDescription("description");
        request.setCreated(LocalDateTime.of(2025, 1, 8, 12, 0, 0));
        request.setOwner(user);

        Mockito.when(requestRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(request));

        Request requestFound = requestService.findById(1L);

        assertEquals(1, requestFound.getId());
        assertEquals("description", requestFound.getDescription());
        assertEquals(LocalDateTime.of(2025, 1, 8, 12, 0, 0),
                requestFound.getCreated());
        assertEquals(1, requestFound.getOwner().getId());
        assertEquals("user", requestFound.getOwner().getName());
        assertEquals("user@mail.com", requestFound.getOwner().getEmail());
    }
}
