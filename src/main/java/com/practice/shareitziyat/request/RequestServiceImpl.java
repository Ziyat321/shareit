package com.practice.shareitziyat.request;

import com.practice.shareitziyat.exceptions.NotFoundException;
import com.practice.shareitziyat.user.User;
import com.practice.shareitziyat.user.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService{
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    @Override
    public Request create(Request request, Long userId) {
        User user = findUserById(userId);
        request.setOwner(user);
        return requestRepository.save(request);
    }

    @Override
    public List<Request> findAllByUser(Long userId) {
        User user = findUserById(userId);
        return requestRepository.findAllByOwnerOrderByCreatedDesc(user);
    }

    @Override
    public List<Request> findAll(int from, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "created");
        PageRequest pageRequest = PageRequest.of(from, size, sort);
        return requestRepository.findAll(pageRequest).getContent();
    }

    @Override
    public Request findById(Long requestId) {
        return requestRepository.findById(requestId).orElseThrow(
                () -> new NotFoundException("Request not found")
        );
    }

    private User findUserById (Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
