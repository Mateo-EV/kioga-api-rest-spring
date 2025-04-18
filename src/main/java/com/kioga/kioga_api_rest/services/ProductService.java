package com.kioga.kioga_api_rest.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.kioga.kioga_api_rest.dto.PaginatedResponseDto;
import com.kioga.kioga_api_rest.entites.Product;

public interface ProductService {
  List<Product> getAllProducts();

  PaginatedResponseDto<Product> getPaginatedAndFilteredActiveProducts(
      Long cursor,
      BigDecimal minPrice,
      BigDecimal maxPrice,
      List<String> categories,
      List<String> brands,
      List<String> availability,
      List<String> subcategories,
      String sortBy);

  List<Product> getBestSellingProductsWeekly();

  Optional<Product> getProductBySlug(String slug);

  List<Product> getSimilarProductsBySlug(String slug);

  Optional<Product> getProductById(Long id);

  Product createProduct(Product product);

  Product updateProduct(Long id, Product product);

  void deleteProduct(Long id);
}
