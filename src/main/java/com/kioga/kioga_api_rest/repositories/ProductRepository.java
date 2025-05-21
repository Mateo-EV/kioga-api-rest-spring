package com.kioga.kioga_api_rest.repositories;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kioga.kioga_api_rest.entites.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
  @Query("""
          SELECT p FROM Product p
          JOIN FETCH p.brand
          JOIN FETCH p.category
          LEFT JOIN FETCH p.subcategory
          WHERE p.isActive = true
          AND (p.price * (1 - p.discount) BETWEEN :min AND :max)
          AND (:cursor IS NULL OR p.id >= :cursor)
          AND (:categories IS NULL OR p.category.slug IN :categories)
          AND (:subcategories IS NULL OR p.subcategory.slug IN :subcategories)
          AND (:brands IS NULL OR p.brand.slug IN :brands)
          AND (
              :availability IS NULL
              OR ('stock' IN :availability AND p.stock > 0)
              OR ('nostock' IN :availability AND p.stock = 0)
          )
      """)
  List<Product> findCursorPaginatedAndFilteredActiveProducts(
      @Param("cursor") Long cursor,
      @Param("min") BigDecimal min,
      @Param("max") BigDecimal max,
      @Param("categories") List<String> categories,
      @Param("brands") List<String> brands,
      @Param("availability") List<String> availability,
      @Param("subcategories") List<String> subcategories,
      Pageable pageable);

  @Query("""
        SELECT p
        FROM Product p
        JOIN FETCH p.category c
        JOIN FETCH p.brand b
        LEFT JOIN FETCH p.subcategory s
        LEFT JOIN OrderProduct op ON op.product = p
        LEFT JOIN op.order o
            ON o.status = 'Sent' AND o.createdAt >= :oneWeekAgo
        GROUP BY p
        ORDER BY COALESCE(SUM(op.quantity), 0) DESC
      """)
  List<Product> findAllWithSalesRanking(@Param("oneWeekAgo") Instant oneWeekAgo, Pageable pageable);

  Optional<Product> findBySlug(String slug);

  @Query("""
        SELECT p FROM Product p
        JOIN FETCH p.brand
        JOIN FETCH p.category
        LEFT JOIN FETCH p.subcategory
        WHERE p.isActive = true
        AND p.id <> :notProductId
        AND (p.category.id = :categoryId OR p.brand.id = :brandId)
        ORDER BY
          CASE
              WHEN p.subcategory.id = :subcategoryId THEN 1
              WHEN p.category.id = :categoryId THEN 2
              WHEN p.brand.id = :brandId THEN 3
          END ASC
      """)
  List<Product> findRelatedProducts(
      Long notProductId,
      Long categoryId,
      Long brandId,
      Long subcategoryId,
      Pageable pageable);
}
