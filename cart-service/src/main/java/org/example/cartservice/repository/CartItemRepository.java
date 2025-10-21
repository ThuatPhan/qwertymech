package org.example.cartservice.repository;

import java.util.Optional;

import org.example.cartservice.entity.CartItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {
    Optional<CartItem> findByProductIdAndCartId(String productId, String cartId);

    Page<CartItem> findAllByCartId(String cartId, Pageable pageable);
}
