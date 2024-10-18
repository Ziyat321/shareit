package com.practice.shareitziyat.item;

import com.practice.shareitziyat.user.User;
import com.practice.shareitziyat.utils.RequestConstants;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

public class ItemController {
    @GetMapping
    public String test(
            @RequestHeader(RequestConstants.USER_HEADER) int userId
    ){
        System.out.println(userId);
        return "user: " + userId;
    }
}
