package com.kioga.kioga_api_rest.services.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kioga.kioga_api_rest.dto.PaginatedResponseDto;
import com.kioga.kioga_api_rest.entites.Product;
import com.kioga.kioga_api_rest.repositories.ProductRepository;
import com.kioga.kioga_api_rest.services.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;

  public ProductServiceImpl(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @Override
  public List<Product> getAllProducts() {
    return productRepository.findAll();
  }

  @Override
  public PaginatedResponseDto<Product> getPaginatedAndFilteredActiveProducts(
      Long cursor,
      BigDecimal minPrice,
      BigDecimal maxPrice,
      List<String> categories,
      List<String> brands,
      List<String> availability,
      List<String> subcategories,
      String sortBy) {
    int limit = 10;

    Pageable pageable = PageRequest.of(0, limit + 1);

    List<Product> products = productRepository.findPaginatedAndFilteredActiveProducts(
        cursor, minPrice, maxPrice, categories, brands, availability, subcategories, pageable);
    Long nextCursor = products.isEmpty() || products.size() < limit + 1 ? null
        : products.get(limit).getId();

    if (nextCursor != null) {
      products.remove(limit);
    }

    return new PaginatedResponseDto<>(products, nextCursor);
  }

  @Override
  public List<Product> getBestSellingProductsWeekly() {
    Pageable pageable = PageRequest.of(0, 10);

    return productRepository.findBestSellingProductsWeekly(
        Instant.now().minus(7, ChronoUnit.DAYS),
        pageable);
  }

  @Override
  public Optional<Product> getProductById(Long id) {
    return productRepository.findById(id);
  }

  @Override
  public Product createProduct(Product product) {
    return productRepository.save(product);
  }

  @Override
  public Product updateProduct(Long id, Product product) {
    product.setId(id);
    return productRepository.save(product);
  }

  @Override
  public void deleteProduct(Long id) {
    Product existingProduct = productRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Product not found"));
    productRepository.delete(existingProduct);
  }

  @Override
  public Optional<Product> getProductBySlug(String slug) {
    return productRepository.findBySlug(slug);
  }

  @Override
  public List<Product> getSimilarProductsBySlug(String slug) {
    Optional<Product> product = getProductBySlug(slug);

    if (product.isPresent()) {
      return productRepository.findRelatedProducts(
          product.get().getId(),
          product.get().getCategory().getId(),
          product.get().getBrand().getId(),
          product.get().getSubcategory() != null ? product.get().getSubcategory().getId() : null,
          Pageable.ofSize(10));
    }

    throw new RuntimeException("Product not found");
  }

}
