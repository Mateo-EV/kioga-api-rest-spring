package com.kioga.kioga_api_rest.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.kioga.kioga_api_rest.dto.order.OrderDto;
import com.kioga.kioga_api_rest.dto.order.StartOrderRequestDto;
import com.kioga.kioga_api_rest.entities.Order;
import com.kioga.kioga_api_rest.entities.OrderProduct;

@Mapper(componentModel = "spring")
public interface OrderMapper {
  OrderDto toDto(Order order);

  List<OrderDto> toDto(List<Order> orders);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "amount", ignore = true)
  @Mapping(target = "shippingAmount", ignore = true)
  @Mapping(target = "user", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "address.order", ignore = true)
  @Mapping(target = "address.orderId", ignore = true)
  Order toEntity(StartOrderRequestDto orderDto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "product.id", source = "productId")
  @Mapping(target = "order", ignore = true)
  @Mapping(target = "unitAmount", ignore = true)
  OrderProduct toEntity(StartOrderRequestDto.ProductStartOrderRequestDto detail);
}
