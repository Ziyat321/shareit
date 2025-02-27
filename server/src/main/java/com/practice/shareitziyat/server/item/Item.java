package com.practice.shareitziyat.server.item;

import com.practice.shareitziyat.server.booking.Booking;
import com.practice.shareitziyat.server.request.Request;
import com.practice.shareitziyat.server.user.User;
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
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

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
    List<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "request_id")
    Request request;

}
