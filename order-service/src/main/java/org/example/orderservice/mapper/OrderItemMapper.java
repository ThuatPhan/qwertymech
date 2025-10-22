package org.example.orderservice.mapper;

import java.util.Map;

import org.example.orderservice.dto.response.OrderItemResponse;
import org.example.orderservice.dto.response.ProductResponse;
import org.example.orderservice.entity.OrderItem;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderItemMapper {
    @Mapping(target = "product", expression = "java(productMap.get(orderItem.getProductId()))")
    OrderItemResponse toOrderItemResponse(OrderItem orderItem, @Context Map<String, ProductResponse> productMap);
}
