package com.practice.shareitziyat.item.dto;

import com.practice.shareitziyat.item.Comment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommentMapper {
    public Comment fromCreate(CommentCreateDto commentCreate){
        Comment comment = new Comment();
        comment.setText(commentCreate.getText());
        return comment;
    }

    public CommentResponseDto toResponse(Comment comment){
        CommentResponseDto commentResponse = new CommentResponseDto();
        commentResponse.setId(comment.getId());
        commentResponse.setText(comment.getText());
        commentResponse.setAuthorName(comment.getUser().getName());
        commentResponse.setCreated(comment.getCreated());
        return commentResponse;
    }

    public List<CommentResponseDto> toResponse(List<Comment> comments){
        return comments.stream().map(this::toResponse).toList();
    }
}
