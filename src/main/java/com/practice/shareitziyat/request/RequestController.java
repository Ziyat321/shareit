package com.practice.shareitziyat.request;

import com.practice.shareitziyat.request.dto.RequestCreateDto;
import com.practice.shareitziyat.request.dto.RequestMapper;
import com.practice.shareitziyat.request.dto.RequestResponseDto;
import com.practice.shareitziyat.utils.RequestConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;
    private final RequestMapper requestMapper;

    @PostMapping
    public RequestResponseDto create (@Valid @RequestBody RequestCreateDto requestCreate,
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
    public List<RequestResponseDto> findAll(@RequestParam int from,
                                            @RequestParam int size) {
        return requestMapper.toResponse(
                requestService.findAll(from, size)
        );
    }

    @GetMapping("/{requestId}")
    public RequestResponseDto findById(@PathVariable Long requestId) {
        return requestMapper.toResponse(
                requestService.findById(requestId)
        );
    }
}
