package com.practice.shareitziyat.server.item;

import com.practice.shareitziyat.server.booking.Booking;
import com.practice.shareitziyat.server.booking.BookingRepository;
import com.practice.shareitziyat.server.booking.BookingStatus;
import com.practice.shareitziyat.server.exceptions.BadRequestException;
import com.practice.shareitziyat.server.exceptions.NotFoundException;
import com.practice.shareitziyat.server.exceptions.ForbiddenException;
import com.practice.shareitziyat.server.item.dto.ItemMapper;
import com.practice.shareitziyat.server.request.Request;
import com.practice.shareitziyat.server.request.RequestRepository;
import com.practice.shareitziyat.server.user.User;
import com.practice.shareitziyat.server.user.UserRepository;
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
    public Item create(Item item, Long userId, Long requestId) {
//        Long requestId = item.getRequest().getId();
        if (requestId != null) {
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
        findUserById(userId);
        Item existingItem = findById(itemId, userId);
        if (!existingItem.getOwner().getId().equals(userId)) {
            throw new ForbiddenException("Wrong owner");
        }
        // проверить, является ли данный пользователь автором предмета (статус 403)
        itemMapper.merge(existingItem, updatedItem);
        itemRepository.save(existingItem);
        return existingItem;
    }

    @Override
    public Item findById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (item.getOwner().getId().equals(userId)) {
            bookingRepository.findLastBooking(itemId, LocalDateTime.now(), BookingStatus.APPROVED)
                    .ifPresent(item::setLastBooking);
            bookingRepository.findNextBooking(itemId, LocalDateTime.now(), BookingStatus.APPROVED)
                    .ifPresent(item::setNextBooking);
        }
        return item;
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

        // TODO инициализировать lastBooking и nextBooking
        return itemRepository.findAllByOwner_Id(userId);
    }

    @Override
    public Comment createComment(Comment comment, Long itemId, Long userId) {

        Booking booking = bookingRepository.findByUser_IdAndItem_IdAndStatusIsAndStartDateBefore(
                userId, itemId, BookingStatus.APPROVED, LocalDateTime.now()
        ).stream().findFirst().orElseThrow(() -> new BadRequestException(".."));
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
//        findById(itemId, userId);
        return commentRepository.findAllByItem_Id(itemId);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
    }
}
