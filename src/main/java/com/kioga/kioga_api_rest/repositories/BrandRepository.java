package com.kioga.kioga_api_rest.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kioga.kioga_api_rest.entities.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long> {

  @Query("""
      SELECT b FROM Brand b
      WHERE b.name LIKE %:search%
      """)
  List<Brand> searchBrands(@Param("search") String search, Pageable pageable);
}
