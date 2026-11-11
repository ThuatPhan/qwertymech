package org.example.orderservice.service;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.example.event.dto.NotificationEvent;
import org.example.orderservice.dto.request.OrderCreationRequest;
import org.example.orderservice.dto.request.OrderItemCreationRequest;
import org.example.orderservice.dto.response.OrderResponse;
import org.example.orderservice.dto.response.PageResponse;
import org.example.orderservice.dto.response.ProductResponse;
import org.example.orderservice.dto.response.UserResponse;
import org.example.orderservice.entity.Order;
import org.example.orderservice.entity.OrderItem;
import org.example.orderservice.enums.OrderStatus;
import org.example.orderservice.enums.PaymentMethod;
import org.example.orderservice.enums.PaymentStatus;
import org.example.orderservice.exception.AppException;
import org.example.orderservice.exception.ErrorCode;
import org.example.orderservice.mapper.OrderMapper;
import org.example.orderservice.repository.OrderRepository;
import org.example.orderservice.repository.httpClient.IdentityClient;
import org.example.orderservice.util.OrderHelper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    IdentityClient identityClient;
    OrderRepository orderRepository;
    OrderMapper orderMapper;
    OrderHelper orderHelper;
    KafkaTemplate<String, Object> kafkaTemplate;
    ObjectMapper objectMapper;

    @Transactional
    @CacheEvict(value = "orders", allEntries = true)
    public OrderResponse createOrder(OrderCreationRequest request) {
        Order order = orderMapper.toOrder(request);

        UserResponse user = identityClient.getUser().getData();
        order.setUserId(user.getId());

        order.setId(orderHelper.generateOrderId());

        PaymentMethod paymentMethod =
                PaymentMethod.valueOf(request.getPaymentMethod().toUpperCase());
        order.setPaymentMethod(paymentMethod);

        Set<String> productIds = request.getItems().stream()
                .map(OrderItemCreationRequest::getProduct)
                .collect(Collectors.toSet());
        Map<String, ProductResponse> productsMap = orderHelper.getProductsMapByIds(productIds);
        Set<OrderItem> orderItems = orderHelper.mapToOrderItems(request.getItems(), productsMap, order);
        order.setItems(orderItems);

        order.setTotalPrice(orderHelper.calculateSubtotal(orderItems));

        Order savedOrder = orderRepository.saveAndFlush(order);

        OrderResponse orderResponse = orderMapper.toOrderResponse(savedOrder, productsMap);

        try {
            kafkaTemplate.send(
                    "notification-delivery",
                    NotificationEvent.builder()
                            .channel("EMAIL")
                            .templateCode("order-confirmation")
                            .recipient(user.getEmail())
                            .param(Map.of(
                                    "order",
                                    objectMapper.writeValueAsString(orderResponse),
                                    "user",
                                    objectMapper.writeValueAsString(user)))
                            .build());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to write JSON: ", e);
        }

        return orderResponse;
    }

    @Cacheable(value = "orders", key = "'id::' + #id")
    public OrderResponse getOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        Set<String> productIds =
                order.getItems().stream().map(OrderItem::getProductId).collect(Collectors.toSet());
        return orderMapper.toOrderResponse(order, orderHelper.getProductsMapByIds(productIds));
    }

    @Cacheable(value = "orders", key = "'page::' + #page + '::size::' + #size + '::all::' + #all")
    public PageResponse<OrderResponse> getOrders(int page, int size, boolean all) {
        UserResponse user = identityClient.getUser().getData();

        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Order> pageData =
                all ? orderRepository.findAll(pageable) : orderRepository.findAllByUserId(pageable, user.getId());

        Set<String> allProductIds = pageData.getContent().stream()
                .flatMap(order -> order.getItems().stream())
                .map(OrderItem::getProductId)
                .collect(Collectors.toSet());
        Map<String, ProductResponse> productsMap = orderHelper.getProductsMapByIds(allProductIds);

        return PageResponse.from(pageData, (order) -> orderMapper.toOrderResponse(order, productsMap));
    }

    @CacheEvict(value = "orders", allEntries = true)
    public void markOrderAsPaid(Long id, PaymentStatus paymentStatus) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        if (paymentStatus.equals(PaymentStatus.SUCCESS)) {
            order.setStatus(OrderStatus.CONFIRMED);
        }
        order.setPaymentStatus(paymentStatus);

        orderRepository.save(order);
    }
}
