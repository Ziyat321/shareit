package com.practice.shareitziyat.gateway.request;

import com.practice.shareitziyat.server.request.dto.RequestCreateDto;
import com.practice.shareitziyat.server.utils.RequestConstants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/requests")
@Validated
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
    public ResponseEntity<Object> findAll(@RequestHeader(RequestConstants.USER_HEADER) Long userId,
                                          @RequestParam(defaultValue = RequestConstants.DEFAULT_FROM) @Min(0) int from,
                                          @RequestParam(defaultValue = RequestConstants.DEFAULT_SIZE) @Min(1) int size) {
        return requestClient.findAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findById(@RequestHeader(RequestConstants.USER_HEADER) Long userId,
                                           @PathVariable Long requestId) {
        return requestClient.findById(userId, requestId);
    }
}
