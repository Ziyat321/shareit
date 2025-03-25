package com.practice.shareitziyat.server.request;

import com.practice.shareitziyat.server.request.dto.RequestCreateDto;
import com.practice.shareitziyat.server.request.dto.RequestMapper;
import com.practice.shareitziyat.server.request.dto.RequestResponseDto;
import com.practice.shareitziyat.server.utils.RequestConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;
    private final RequestMapper requestMapper;

    @PostMapping
    public RequestResponseDto create(@RequestBody RequestCreateDto requestCreate,
                                     @RequestHeader(RequestConstants.USER_HEADER) Long userId) {
        return requestMapper.toResponse(
                requestService.create(requestMapper.fromCreate(requestCreate), userId)
        );
    }

    @GetMapping
    public List<RequestResponseDto> findAllByUser(@RequestHeader(RequestConstants.USER_HEADER) Long userId) {
        return requestMapper.toResponse(
                requestService.findAllByUser(userId)
        );
    }

    @GetMapping("/all")
    public List<RequestResponseDto> findAll(@RequestHeader(RequestConstants.USER_HEADER) Long userId,
                                            @RequestParam(defaultValue = RequestConstants.DEFAULT_FROM) int from,
                                            @RequestParam(defaultValue = RequestConstants.DEFAULT_SIZE) int size) {
        return requestMapper.toResponse(
                requestService.findAll(userId, from, size)
        );
    }

    @GetMapping("/{requestId}")
    public RequestResponseDto findById(@RequestHeader(RequestConstants.USER_HEADER) Long userId,
                                       @PathVariable Long requestId) {
        return requestMapper.toResponse(
                requestService.findById(userId, requestId)
        );
    }
}
