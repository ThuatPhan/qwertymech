package org.example.orderservice.util;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.example.orderservice.dto.request.OrderItemCreationRequest;
import org.example.orderservice.dto.response.ProductResponse;
import org.example.orderservice.entity.Order;
import org.example.orderservice.entity.OrderItem;
import org.example.orderservice.repository.httpClient.ProductClient;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderHelper {
    ProductClient productClient;

    public long generateOrderId() {
        String timePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));
        long randomPart = ThreadLocalRandom.current().nextLong(100, 999);
        return Long.parseLong(timePart + randomPart);
    }

    public BigDecimal calculateSubtotal(Set<OrderItem> orderItems) {
        return orderItems.stream().map(OrderItem::getSubTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Set<OrderItem> mapToOrderItems(
            Set<OrderItemCreationRequest> itemsRequest, Map<String, ProductResponse> productsMap, Order order) {
        return itemsRequest.stream()
                .map(req -> {
                    ProductResponse product = productsMap.get(req.getProduct());

                    return OrderItem.builder()
                            .productId(product.getId())
                            .quantity(req.getQuantity())
                            .unitPrice(product.getPrice())
                            .subTotal(product.getPrice().multiply(BigDecimal.valueOf(req.getQuantity())))
                            .order(order)
                            .build();
                })
                .collect(Collectors.toSet());
    }

    public Map<String, ProductResponse> getProductsMapByIds(Set<String> productIds) {
        List<ProductResponse> products =
                productClient.getProductByIds(productIds).getData();

        return products.stream().collect(Collectors.toMap(ProductResponse::getId, Function.identity()));
    }
}
