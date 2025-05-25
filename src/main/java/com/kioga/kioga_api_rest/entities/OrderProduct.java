package com.kioga.kioga_api_rest.entities;

import java.math.BigDecimal;

import org.hibernate.annotations.ColumnDefault;

import com.kioga.kioga_api_rest.entities.embeds.OrderProductId;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderProduct {
  @EmbeddedId
  private OrderProductId id;

  @ManyToOne(optional = false)
  @MapsId("orderId")
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;

  @ManyToOne(optional = false)
  @MapsId("productId")
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @Column(nullable = false)
  @ColumnDefault("1")
  private int quantity;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal unitAmount;
}