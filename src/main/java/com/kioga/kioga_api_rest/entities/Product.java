package com.kioga.kioga_api_rest.entities;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String slug;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal price;

  @Column(nullable = true, precision = 3, scale = 2)
  private BigDecimal discount;

  @Column(nullable = false)
  private String image;

  @Column(nullable = false)
  @ColumnDefault("0")
  private Integer stock;

  @ManyToOne
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @ManyToOne
  @JoinColumn(name = "subcategory_id", nullable = true)
  private Subcategory subcategory;

  @ManyToOne
  @JoinColumn(name = "brand_id", nullable = false)
  private Brand brand;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderProduct> orderProducts;

  @Column(nullable = false)
  @ColumnDefault("true")
  private Boolean isActive;

  @CreationTimestamp
  private Instant createdAt;

  @UpdateTimestamp
  private Instant updatedAt;
}
