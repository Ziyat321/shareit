package com.practice.shareitziyat.item;

import com.practice.shareitziyat.booking.Booking;
import com.practice.shareitziyat.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String name;

    String description;

    @Column(name = "is_available")
    Boolean available;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    User owner;

    @OneToMany(mappedBy = "item")
    List<Booking> bookings;

    @OneToMany(mappedBy = "item")
    List<Comment> items;

    @OneToMany(mappedBy = "user")
    List<User> users;
}
