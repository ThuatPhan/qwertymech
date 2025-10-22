package org.example.orderservice.mapper;

import java.util.Map;

import org.example.orderservice.dto.request.OrderCreationRequest;
import org.example.orderservice.dto.response.OrderResponse;
import org.example.orderservice.dto.response.ProductResponse;
import org.example.orderservice.entity.Order;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {OrderItemMapper.class})
public interface OrderMapper {
    @Mapping(target = "paymentMethod", ignore = true)
    Order toOrder(OrderCreationRequest request);

    OrderResponse toOrderResponse(Order order, @Context Map<String, ProductResponse> productMap);
}
