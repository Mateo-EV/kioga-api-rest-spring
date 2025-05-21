package com.kioga.kioga_api_rest.services;

import java.math.BigDecimal;
import java.util.List;

import com.kioga.kioga_api_rest.dto.CursorPaginatedResponseDto;
import com.kioga.kioga_api_rest.dto.product.ProductDto;
import com.kioga.kioga_api_rest.entites.Product;

public interface ProductService {
  List<ProductDto> getAllProducts();

  CursorPaginatedResponseDto<ProductDto> getPaginatedAndFilteredActiveProducts(
      Long cursor,
      BigDecimal minPrice,
      BigDecimal maxPrice,
      List<String> categories,
      List<String> brands,
      List<String> availability,
      List<String> subcategories,
      String sortBy);

  List<ProductDto> getBestSellingProductsWeekly();

  ProductDto getProductBySlug(String slug);

  List<ProductDto> getSimilarProductsBySlug(String slug);

  ProductDto getProductById(Long id);

  ProductDto createProduct(Product product);

  ProductDto updateProduct(Long id, Product product);

  void deleteProduct(Long id);
}
