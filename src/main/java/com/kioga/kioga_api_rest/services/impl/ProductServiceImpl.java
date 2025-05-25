package com.kioga.kioga_api_rest.services.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kioga.kioga_api_rest.dto.CursorPaginatedResponseDto;
import com.kioga.kioga_api_rest.dto.product.ProductDto;
import com.kioga.kioga_api_rest.entities.Product;
import com.kioga.kioga_api_rest.mappers.ProductMapper;
import com.kioga.kioga_api_rest.repositories.ProductRepository;
import com.kioga.kioga_api_rest.services.ProductService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final ProductMapper productMapper;

  @Override
  public List<ProductDto> getAllProducts() {
    return productMapper.toDto(productRepository.findAll());
  }

  private Sort getSortByPattern(String sortBy) {
    String fieldToSort;
    Direction direction;

    if (sortBy != null) {
      if (sortBy.startsWith("name")) {
        fieldToSort = "name";
      } else if (sortBy.startsWith("price")) {
        fieldToSort = "price";
      } else {
        fieldToSort = null;
      }

      direction = sortBy.endsWith("asc") ? Direction.ASC : Direction.DESC;
    } else {
      fieldToSort = "id";
      direction = Direction.DESC;
    }

    return fieldToSort != null ? Sort.by(direction, fieldToSort) : null;
  }

  @Override
  public CursorPaginatedResponseDto<ProductDto> getPaginatedAndFilteredActiveProducts(
      Long cursor,
      BigDecimal minPrice,
      BigDecimal maxPrice,
      List<String> categories,
      List<String> brands,
      List<String> availability,
      List<String> subcategories,
      String sortBy) {
    final int limit = 10;

    Sort sort = getSortByPattern(sortBy);
    Pageable pageable = PageRequest.of(0, limit + 1, sort);

    List<Product> products = sort != null
        ? productRepository.findCursorPaginatedAndFilteredActiveProducts(
            cursor,
            minPrice,
            maxPrice,
            categories,
            brands,
            availability,
            subcategories,
            pageable)
        : productRepository.findCursorPaginatedAndFilteredActiveProductsSortByOrder(
            cursor,
            minPrice,
            maxPrice,
            categories,
            brands,
            availability,
            subcategories,
            pageable);
    Long nextCursor = products.isEmpty() || products.size() < limit + 1 ? null
        : products.get(limit).getId();

    if (nextCursor != null) {
      products.remove(limit);
    }

    return new CursorPaginatedResponseDto<>(productMapper.toDto(products), nextCursor);
  }

  @Override
  public List<ProductDto> getBestSellingProductsWeekly() {
    Pageable pageable = PageRequest.of(0, 10);

    List<Product> products = productRepository.findAllWithSalesRanking(
        Instant.now().minus(7, ChronoUnit.DAYS),
        pageable);

    return productMapper.toDto(products);
  }

  @Override
  public ProductDto getProductById(Long id) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));
    return productMapper.toDto(product);
  }

  @Override
  public ProductDto createProduct(Product product) {
    return productMapper.toDto(productRepository.save(product));
  }

  @Override
  public ProductDto updateProduct(Long id, Product product) {
    product.setId(id);
    return productMapper.toDto(productRepository.save(product));
  }

  @Override
  public void deleteProduct(Long id) {
    Product existingProduct = productRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    productRepository.delete(existingProduct);
  }

  @Override
  public ProductDto getProductBySlug(String slug) {
    Product product = productRepository.findBySlug(slug)
        .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));
    return productMapper.toDto(product);
  }

  @Override
  public List<ProductDto> getSimilarProductsBySlug(String slug) {
    Product product = productRepository.findBySlug(slug)
        .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

    List<Product> similarProducts = productRepository.findRelatedProducts(
        product.getId(),
        product.getCategory().getId(),
        product.getBrand().getId(),
        product.getSubcategory() != null ? product.getSubcategory().getId() : null,
        Pageable.ofSize(10));

    return productMapper.toDto(similarProducts);
  }

}
