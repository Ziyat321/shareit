package com.practice.shareitziyat.user;

import com.practice.shareitziyat.booking.Booking;
import com.practice.shareitziyat.item.Comment;
import com.practice.shareitziyat.item.Item;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    Long id;

    String name;

    String email;

    @OneToMany(mappedBy = "owner")
    List<Item> items;

    @OneToMany(mappedBy = "user")
    List<Booking> bookings;

    @OneToMany(mappedBy = "user")
    List<Comment> comments;


}
