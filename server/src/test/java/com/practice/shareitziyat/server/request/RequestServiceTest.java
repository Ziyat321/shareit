package com.practice.shareitziyat.server.request;

import com.practice.shareitziyat.server.comparators.RequestCreatedDateComparator;
import com.practice.shareitziyat.server.exceptions.NotFoundException;
import com.practice.shareitziyat.server.item.Item;
import com.practice.shareitziyat.server.item.dto.ItemMapper;
import com.practice.shareitziyat.server.request.dto.RequestMapper;
import com.practice.shareitziyat.server.request.dto.RequestResponseDto;
import com.practice.shareitziyat.server.user.User;
import com.practice.shareitziyat.server.user.UserRepository;
import com.practice.shareitziyat.server.user.dto.UserMapper;
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
    public void createTest1() {
        RequestService requestService = new RequestServiceImpl(requestRepository, userRepository, requestMapper);
        Request request = new Request();
        request.setId(1L);
        request.setDescription("description");
        request.setCreated(LocalDateTime.of(2025, 2, 4, 12, 0, 0));

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> requestService.create(request, 1L));

        assertEquals("User not found", exception.getMessage());
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
                        .sorted(new RequestCreatedDateComparator())
                        .toList());

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

        Mockito.when(requestRepository.findAllByOwnerIdNot(Mockito.anyLong(), Mockito.any(PageRequest.class)))
                .thenAnswer(invocationOnMock -> {
                    List<Request> requests1 = (requests.stream()
                            .sorted(new RequestCreatedDateComparator()).toList());
                    return new PageImpl<>(requests1);
                });

        List<Request> requestList = requestService.findAll(2L, 0, 2);

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

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(requestRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(request));

        Request requestFound = requestService.findById(2L, 1L);

        assertEquals(1, requestFound.getId());
        assertEquals("description", requestFound.getDescription());
        assertEquals(LocalDateTime.of(2025, 1, 8, 12, 0, 0),
                requestFound.getCreated());
        assertEquals(1, requestFound.getOwner().getId());
        assertEquals("user", requestFound.getOwner().getName());
        assertEquals("user@mail.com", requestFound.getOwner().getEmail());
    }

    @Test
    public void findByIdExceptionTest() {
        RequestService requestService = new RequestServiceImpl(requestRepository, userRepository, requestMapper);

        Mockito.when(userRepository.findById(Mockito.anyLong())).
                thenAnswer(invocationOnMock -> {
                    long userId = invocationOnMock.getArgument(0);
                    User user = new User();
                    user.setId(userId);
                    user.setName("user");
                    user.setEmail("user@mail.com");
                    return Optional.of(user);
                });
        Mockito.when(requestRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> requestService.findById(2L, 1L));

        assertEquals("Request not found", exception.getMessage());
    }

    @Test
    public void toResponseTest() {
        itemMapper = new ItemMapper();
        userMapper = new UserMapper();
        requestMapper = new RequestMapper(itemMapper, userMapper);
        Request request = new Request();
        request.setId(1L);
        request.setDescription("description");
        request.setCreated(LocalDateTime.of(2025, 2, 9, 12, 0, 0));
        User owner = new User();
        owner.setId(1L);
        owner.setName("user");
        owner.setEmail("user@email.com");
        request.setOwner(owner);
        Item item = new Item();
        item.setId(1L);
        item.setName("name");
        item.setDescription("text");
        item.setAvailable(true);
        List<Item> items = List.of(item);
        request.setItems(items);

        RequestResponseDto requestResponse = requestMapper.toResponse(request);

        assertEquals(1, requestResponse.getId());
        assertEquals("description", requestResponse.getDescription());
        assertEquals(LocalDateTime.of(2025, 2, 9, 12, 0, 0),
                requestResponse.getCreated());
        assertEquals(1, requestResponse.getOwner().getId());
        assertEquals("user", requestResponse.getOwner().getName());
        assertEquals("user@email.com", requestResponse.getOwner().getEmail());
        assertEquals(1, requestResponse.getItems().get(0).getId());
        assertEquals("name", requestResponse.getItems().get(0).getName());
        assertEquals("text", requestResponse.getItems().get(0).getDescription());
        assertEquals(true, requestResponse.getItems().get(0).getAvailable());
    }

    @Test
    public void toResponseTest1() {
        itemMapper = new ItemMapper();
        userMapper = new UserMapper();
        requestMapper = new RequestMapper(itemMapper, userMapper);
        User owner = new User();
        owner.setId(1L);
        owner.setName("user");
        owner.setEmail("user@email.com");
        Request request1 = new Request();
        request1.setId(1L);
        request1.setDescription("description1");
        request1.setCreated(LocalDateTime.of(2025, 2, 9, 12, 0, 0));
        request1.setOwner(owner);
        Request request2 = new Request();
        request2.setId(2L);
        request2.setDescription("description2");
        request2.setCreated(LocalDateTime.of(2025, 2, 8, 12, 0, 0));
        request2.setOwner(owner);
        List<Request> requestList = List.of(request1, request2);
        Page<Request> requests = new PageImpl<>(requestList);

        Page<RequestResponseDto> requestResponse = requestMapper.toResponse(requests);
        List<RequestResponseDto> requestResponseList = requestResponse.stream().toList();

        assertEquals(2, requestResponseList.size());
        assertEquals(1, requestResponseList.get(0).getId());
        assertEquals("description1", requestResponseList.get(0).getDescription());
        assertEquals(LocalDateTime.of(2025, 2, 9, 12, 0, 0),
                requestResponseList.get(0).getCreated());
        assertEquals(1, requestResponseList.get(0).getOwner().getId());
        assertEquals("user", requestResponseList.get(0).getOwner().getName());
        assertEquals("user@email.com", requestResponseList.get(0).getOwner().getEmail());
        assertEquals(2, requestResponseList.get(1).getId());
        assertEquals("description2", requestResponseList.get(1).getDescription());
        assertEquals(LocalDateTime.of(2025, 2, 8, 12, 0, 0),
                requestResponseList.get(1).getCreated());
        assertEquals(1, requestResponseList.get(1).getOwner().getId());
        assertEquals("user", requestResponseList.get(1).getOwner().getName());
        assertEquals("user@email.com", requestResponseList.get(1).getOwner().getEmail());
    }
}
