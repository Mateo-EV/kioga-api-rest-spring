package com.kioga.kioga_api_rest.entities.embeds;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class OrderProductId {
  private Long orderId;
  private Long productId;
}