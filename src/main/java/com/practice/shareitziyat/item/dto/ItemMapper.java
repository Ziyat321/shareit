package com.practice.shareitziyat.item.dto;

import com.practice.shareitziyat.item.Comment;
import com.practice.shareitziyat.item.Item;
import com.practice.shareitziyat.request.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemMapper {

//    private final CommentMapper commentMapper;

    public Item fromCreate(ItemCreateDto itemCreate) {
        Item item = new Item();
        item.setName(itemCreate.getName());
        item.setDescription(itemCreate.getDescription());
        item.setAvailable(itemCreate.getAvailable());
//        Request request = new Request();
//        request.setId(itemCreate.getRequestId());
//        item.setRequest(request);
        return item;
    }

    public Item fromUpdate(ItemUpdateDto itemUpdate) {
        Item item = new Item();
        item.setName(itemUpdate.getName());
        item.setDescription(itemUpdate.getDescription());
        item.setAvailable(itemUpdate.getAvailable());
        return item;
    }

    public ItemResponseDto toResponse(Item item) {
        ItemResponseDto itemResponse = new ItemResponseDto();
        itemResponse.setId(item.getId());
        itemResponse.setName(item.getName());
        itemResponse.setDescription(item.getDescription());
        itemResponse.setAvailable(item.getAvailable());
        if(item.getComments() != null) {
            itemResponse.setComments(toResponseComment(item.getComments()));
        }
        if(item.getRequest() != null) {
            itemResponse.setRequestId(item.getRequest().getId());
        }
        return itemResponse;
    }

    public List<ItemResponseDto> toResponse(List<Item> items) {
        return items.stream()
                .map(this::toResponse)
                .toList();
    }

    public void merge(Item existingItem, Item updatedItem) {
        if (updatedItem.getName() != null) {
            existingItem.setName(updatedItem.getName());
        }
        if (updatedItem.getDescription() != null) {
            existingItem.setDescription(updatedItem.getDescription());
        }
        if (updatedItem.getAvailable() != null) {
            existingItem.setAvailable(updatedItem.getAvailable());
        }
        if (updatedItem.getOwner() != null) {
            existingItem.setOwner(updatedItem.getOwner());
        }
    }

    public Comment fromCreateComment(CommentCreateDto commentCreate){
        Comment comment = new Comment();
        comment.setText(commentCreate.getText());
        return comment;
    }

    public CommentResponseDto toResponseComment(Comment comment){
        CommentResponseDto commentResponse = new CommentResponseDto();
        commentResponse.setId(comment.getId());
        commentResponse.setText(comment.getText());
        commentResponse.setAuthorName(comment.getUser().getName());
        commentResponse.setCreated(comment.getCreated());
        return commentResponse;
    }

    public List<CommentResponseDto> toResponseComment(List<Comment> comments){
        return comments.stream().map(this::toResponseComment).toList();
    }
}
