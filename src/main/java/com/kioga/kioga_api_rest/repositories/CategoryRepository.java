package com.kioga.kioga_api_rest.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kioga.kioga_api_rest.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  @Query("""
      SELECT c FROM Category c
      JOIN c.products p
      LEFT JOIN OrderProduct op ON op.product = p
      LEFT JOIN op.order o ON o.status = 'Sent'
      GROUP BY c
      ORDER BY COALESCE(SUM(op.quantity), 0)
      """)
  List<Category> findTopCategories(Pageable pageable);
}
