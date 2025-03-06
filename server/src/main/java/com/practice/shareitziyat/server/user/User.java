package com.practice.shareitziyat.server.user;

import com.practice.shareitziyat.server.booking.Booking;
import com.practice.shareitziyat.server.item.Comment;
import com.practice.shareitziyat.server.item.Item;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    @Column(unique = true)
    String email;

    @OneToMany(mappedBy = "owner")
    List<Item> items;

    @OneToMany(mappedBy = "user")
    List<Booking> bookings;

    @OneToMany(mappedBy = "user")
    List<Comment> comments;
}
