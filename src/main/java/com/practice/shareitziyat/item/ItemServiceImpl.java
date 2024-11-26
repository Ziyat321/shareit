package com.practice.shareitziyat.item;

import com.practice.shareitziyat.booking.Booking;
import com.practice.shareitziyat.booking.BookingRepository;
import com.practice.shareitziyat.booking.BookingStatus;
import com.practice.shareitziyat.exceptions.BadRequestException;
import com.practice.shareitziyat.exceptions.NotFoundException;
import com.practice.shareitziyat.exceptions.ForbiddenException;
import com.practice.shareitziyat.item.dto.ItemMapper;
import com.practice.shareitziyat.request.Request;
import com.practice.shareitziyat.request.RequestRepository;
import com.practice.shareitziyat.user.User;
import com.practice.shareitziyat.user.UserRepository;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Data
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final RequestRepository requestRepository;
    private final ItemMapper itemMapper;

    @Override

    public Item create(Item item, Long userId) {
        Long requestId = item.getRequest().getId();
        if(requestId != null) {
            Request request = requestRepository.findById(requestId)
                    .orElseThrow(() -> new NotFoundException("Request not found"));
            item.setRequest(request);
        }
        // существует ли пользователь userId
        User found = findUserById(userId);
        item.setOwner(found);
        return itemRepository.save(item);
    }

    @Override
    public Item update(Item updatedItem, Long itemId, Long userId) {
        // существует ли пользователь userId
        User found = findUserById(userId);
        Item existingItem = findById(itemId);
        if (existingItem.getOwner().getId() != userId) {
            throw new ForbiddenException("Wrong owner");
        }
        // проверить, является ли данный пользователь автором предмета (статус 403)
        itemMapper.merge(existingItem, updatedItem);
        itemRepository.save(existingItem);
        return existingItem;
    }

    @Override
    public Item findById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public List<Item> search(String text) {
        if (StringUtils.isBlank(text)) {
            return new ArrayList<>();
        }
        return itemRepository.search(text);
    }

    @Override
    public void deleteById(Long itemId) {
        itemRepository.deleteById(itemId);
    }

    @Override
    public List<Item> findAll(Long userId) {
        // существует ли пользователь userId
        findUserById(userId);
        return itemRepository.findAllByOwner_Id(userId);
    }

    @Override
    public Comment createComment(Comment comment, Long itemId, Long userId) {

        Booking booking = bookingRepository.findByUser_IdAndItem_IdAndStatusIsAndStartDateBefore(
                userId, itemId, BookingStatus.APPROVED, LocalDateTime.now()
        ).stream().findFirst().orElseThrow(()-> new BadRequestException(".."));
        Item item = booking.getItem();
        User user = booking.getUser();

        comment.setItem(item);
        comment.setUser(user);
        comment.setCreated(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> findCommentsByUser(Long userId) {
        findUserById(userId);
        return commentRepository.findAllByUser_Id(userId);
    }

    @Override
    public List<Comment> findCommentsByItem(Long itemId) {
        findById(itemId);
        return commentRepository.findAllByItem_Id(itemId);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
    }
}
