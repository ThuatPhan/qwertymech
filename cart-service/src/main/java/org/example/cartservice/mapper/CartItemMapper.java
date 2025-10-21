package org.example.cartservice.mapper;

import org.example.cartservice.dto.response.CartItemResponse;
import org.example.cartservice.dto.response.ProductResponse;
import org.example.cartservice.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CartItemMapper {
    @Mapping(target = "id", source = "cartItem.id")
    @Mapping(target = "product", source = "product")
    CartItemResponse mapToCartItemResponse(CartItem cartItem, ProductResponse product);
}
