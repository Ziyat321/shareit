package com.practice.shareitziyat.gateway.request;

import com.practice.shareitziyat.server.request.dto.RequestCreateDto;
import com.practice.shareitziyat.server.utils.RequestConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/requests")
public class RequestController {
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody RequestCreateDto requestCreate,
                                         @RequestHeader(RequestConstants.USER_HEADER) Long userId) {
        return requestClient.create(requestCreate, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllByUser(@RequestHeader(RequestConstants.USER_HEADER) Long userId) {
        return requestClient.findAllByUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAll(@RequestParam int from,
                                          @RequestParam int size) {
        return requestClient.findAll(from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findById(@PathVariable Long requestId) {
        return requestClient.findById(requestId);
    }
}
