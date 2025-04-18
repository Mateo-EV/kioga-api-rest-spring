package com.kioga.kioga_api_rest.controllers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.kioga.kioga_api_rest.dto.PaginatedResponseDto;
import com.kioga.kioga_api_rest.dto.product.ProductDto;
import com.kioga.kioga_api_rest.entites.Product;
import com.kioga.kioga_api_rest.mappers.ProductMapper;
import com.kioga.kioga_api_rest.services.ProductService;

@RestController()
@RequestMapping("/products")
public class ProductController {
  private final ProductService productService;
  private final ProductMapper productMapper;

  public ProductController(ProductService productService, ProductMapper productMapper) {
    this.productService = productService;
    this.productMapper = productMapper;
  }

  @GetMapping
  public PaginatedResponseDto<ProductDto> getPaginatedAndFilteredProducts(
      @RequestParam(required = false) Long cursor,
      @RequestParam(required = false, defaultValue = "0") BigDecimal minPrice,
      @RequestParam(required = false, defaultValue = "4000") BigDecimal maxPrice,
      @RequestParam(required = false) List<String> categories,
      @RequestParam(required = false) List<String> brands,
      @RequestParam(required = false) List<String> availability,
      @RequestParam(required = false) List<String> subcategories,
      @RequestParam(required = false) String sortBy) {

    PaginatedResponseDto<Product> productsPaginated = productService.getPaginatedAndFilteredActiveProducts(
        cursor,
        minPrice,
        maxPrice,
        categories,
        brands,
        availability,
        subcategories,
        sortBy);
    List<ProductDto> productDtos = productsPaginated.getItems().stream()
        .map(productMapper::toDto).toList();

    return new PaginatedResponseDto<ProductDto>(productDtos, productsPaginated.getNextCursor());
  }

  @GetMapping("/top-weekly")
  public List<ProductDto> getAllProducts() {
    return productService.getBestSellingProductsWeekly().stream().map(productMapper::toDto).toList();
  }

  @GetMapping("/{slug}")
  public ProductDto getProductBySlug(@PathVariable("slug") String slug) {
    return productService.getProductBySlug(slug).map(productMapper::toDto)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }

  @GetMapping("/{slug}/similar")
  public List<ProductDto> getSimilarProducts(@PathVariable("slug") String slug) {
    return productService.getSimilarProductsBySlug(slug).stream().map(productMapper::toDto).toList();
  }

}
