package com.kioga.kioga_api_rest.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kioga.kioga_api_rest.dto.category.CategoryDto;
import com.kioga.kioga_api_rest.dto.category.CategoryWithProductsDto;
import com.kioga.kioga_api_rest.services.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
  private final CategoryService categoryService;

  @GetMapping
  public List<CategoryDto> getAllCategories() {
    return categoryService.getAllCategories();
  }

  @GetMapping("/top-bestseller")
  public List<CategoryWithProductsDto> getTopCategories() {
    return categoryService.getTopCategories();
  }

}
