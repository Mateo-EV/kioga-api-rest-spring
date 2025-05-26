package com.kioga.kioga_api_rest.controllers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kioga.kioga_api_rest.dto.CursorPaginatedResponseDto;
import com.kioga.kioga_api_rest.dto.product.ProductDto;
import com.kioga.kioga_api_rest.services.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
  private final ProductService productService;

  @GetMapping
  public CursorPaginatedResponseDto<ProductDto> getPaginatedAndFilteredProducts(
      @RequestParam(required = false) Long cursor,
      @RequestParam(required = false, defaultValue = "5") BigDecimal minPrice,
      @RequestParam(required = false, defaultValue = "4000") BigDecimal maxPrice,
      @RequestParam(required = false) List<String> categories,
      @RequestParam(required = false) List<String> brands,
      @RequestParam(required = false) List<String> availability,
      @RequestParam(required = false) List<String> subcategories,
      @RequestParam(required = false, name = "orderBy") String sortBy) {

    CursorPaginatedResponseDto<ProductDto> productsPaginated = productService.getPaginatedAndFilteredActiveProducts(
        cursor,
        minPrice,
        maxPrice,
        categories,
        brands,
        availability,
        subcategories,
        sortBy);

    return productsPaginated;
  }

  @GetMapping("/top-weekly")
  public List<ProductDto> getAllProducts() {
    return productService.getBestSellingProductsWeekly();
  }

  @GetMapping("/{slug}")
  public ProductDto getProductBySlug(@PathVariable String slug) {
    return productService.getProductBySlug(slug);
  }

  @GetMapping("/{slug}/similar")
  public List<ProductDto> getSimilarProducts(@PathVariable String slug) {
    return productService.getSimilarProductsBySlug(slug);
  }

}
