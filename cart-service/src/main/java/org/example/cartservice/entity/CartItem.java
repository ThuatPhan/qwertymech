package org.example.cartservice.entity;

import java.time.Instant;

import jakarta.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    Cart cart;

    @Column(nullable = false, unique = true)
    String productId;

    @Column(nullable = false)
    Integer quantity;

    @CreationTimestamp
    Instant addedAt;
}
