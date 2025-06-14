package com.kioga.kioga_api_rest.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kioga.kioga_api_rest.entities.Order;
import com.kioga.kioga_api_rest.entities.User;

public interface OrderRepository extends JpaRepository<Order, Long> {
  @EntityGraph(attributePaths = { "user", "orderProducts.product" })
  List<Order> findByUser(User user);
}
