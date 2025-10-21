package org.example.cartservice.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.example.cartservice.dto.request.CartItemCreationRequest;
import org.example.cartservice.dto.response.*;
import org.example.cartservice.entity.Cart;
import org.example.cartservice.entity.CartItem;
import org.example.cartservice.exception.AppException;
import org.example.cartservice.exception.ErrorCode;
import org.example.cartservice.mapper.CartItemMapper;
import org.example.cartservice.repository.CartItemRepository;
import org.example.cartservice.repository.CartRepository;
import org.example.cartservice.repository.httpClient.IdentityClient;
import org.example.cartservice.repository.httpClient.ProductClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartService {
    IdentityClient identityClient;
    ProductClient productClient;
    CartRepository cartRepository;
    CartItemRepository cartItemRepository;
    CartItemMapper cartItemMapper;

    @Transactional
    public CartItemResponse addCartItem(CartItemCreationRequest request) {
        ProductResponse product =
                productClient.getProductById(request.getProduct()).getData();
        UserResponse user = identityClient.getUser().getData();

        Cart cart = cartRepository
                .getByUserId(user.getId())
                .orElseGet(() ->
                        cartRepository.save(Cart.builder().userId(user.getId()).build()));

        CartItem cartItem = cartItemRepository
                .findByProductIdAndCartId(product.getId(), cart.getId())
                .map(item -> {
                    item.setQuantity(item.getQuantity() + request.getQuantity());
                    return item;
                })
                .orElseGet(() -> CartItem.builder()
                        .cart(cart)
                        .productId(product.getId())
                        .quantity(request.getQuantity())
                        .build());

        CartItem savedItem = cartItemRepository.save(cartItem);

        return cartItemMapper.mapToCartItemResponse(savedItem, product);
    }

    public PageResponse<CartItemResponse> getCartItems(int page, int size) {
        UserResponse user = identityClient.getUser().getData();

        Optional<Cart> cartOpt = cartRepository.getByUserId(user.getId());
        if (cartOpt.isEmpty()) {
            return PageResponse.empty(page, size);
        }

        Sort sort = Sort.by("addedAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<CartItem> pageData =
                cartItemRepository.findAllByCartId(cartOpt.get().getId(), pageable);

        if (pageData.getContent().isEmpty()) {
            return PageResponse.empty(page, size);
        }

        List<ProductResponse> products = productClient
                .getProductByIds(pageData.getContent().stream()
                        .map(CartItem::getProductId)
                        .collect(Collectors.toSet()))
                .getData();
        // Convert to Map for faster product lookup
        Map<String, ProductResponse> productsMap =
                products.stream().collect(Collectors.toMap(ProductResponse::getId, product -> product));

        return PageResponse.from(
                pageData,
                (cartItem) -> cartItemMapper.mapToCartItemResponse(cartItem, productsMap.get(cartItem.getProductId())));
    }

    public CartItemResponse updateItemQuantity(String id, int quantity) {
        CartItem cartItem =
                cartItemRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));

        cartItem.setQuantity(quantity);

        CartItem savedItem = cartItemRepository.save(cartItem);

        ProductResponse productResponse = productClient.getProductById(cartItem.getProductId()).getData();

        return cartItemMapper.mapToCartItemResponse(savedItem, productResponse);
    }

    public void deleteItem(String id) {
        CartItem cartItem =
                cartItemRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));

        cartItemRepository.delete(cartItem);
    }
}
