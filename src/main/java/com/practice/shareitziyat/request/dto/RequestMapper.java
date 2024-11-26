package com.practice.shareitziyat.request.dto;


import com.practice.shareitziyat.item.dto.ItemMapper;
import com.practice.shareitziyat.request.Request;
import com.practice.shareitziyat.user.dto.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RequestMapper {
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;

    public Request fromCreate(RequestCreateDto requestCreate) {
        Request request = new Request();
        request.setDescription(requestCreate.getDescription());
        return request;
    }

    public RequestResponseDto toResponse(Request request) {
        RequestResponseDto requestResponseDto = new RequestResponseDto();
        requestResponseDto.setId(request.getId());
        requestResponseDto.setDescription(request.getDescription());
        requestResponseDto.setCreated(request.getCreated());
        if(request.getItems() != null) {
            requestResponseDto.setItems(itemMapper.toResponse(request.getItems()));
        }
        requestResponseDto.setOwner(userMapper.toResponse(request.getOwner()));
        return requestResponseDto;
    }

    public List<RequestResponseDto> toResponse(List<Request> requests) {
        return requests.stream()
                .map(this::toResponse)
                .toList();
    }
    public Page<RequestResponseDto> toResponse(Page<Request> requests) {
        return requests.map(this::toResponse);
    }
}
