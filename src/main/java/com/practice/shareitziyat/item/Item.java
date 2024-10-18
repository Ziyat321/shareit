package com.practice.shareitziyat.item;

import com.practice.shareitziyat.request.Request;
import com.practice.shareitziyat.user.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {
    int id;
    String name;
    String description;
    boolean available;
    User owner;
    Request request;
}
