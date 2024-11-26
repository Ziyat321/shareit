package com.practice.shareitziyat.request;

import com.practice.shareitziyat.item.Item;
import com.practice.shareitziyat.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String description;

    @CreationTimestamp
    LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    User owner;

    @OneToMany(mappedBy = "request")
    List<Item> items;
}
