package com.kioga.kioga_api_rest.services;

import java.util.List;

import com.kioga.kioga_api_rest.dto.order.OrderDto;
import com.kioga.kioga_api_rest.dto.order.StartOrderRequestDto;
import com.kioga.kioga_api_rest.entities.User;

public interface OrderService {
  List<OrderDto> getOrdersByUser(User user);

  OrderDto startOrder(StartOrderRequestDto request, User user);

  // void updateOrderStatus();

  // void getOrderById();

  // void getOrdersPaginatedByStatus();

  // void deleteOrderById();
}
