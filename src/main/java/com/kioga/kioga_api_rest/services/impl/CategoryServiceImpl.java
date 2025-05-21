package com.kioga.kioga_api_rest.services.impl;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kioga.kioga_api_rest.dto.category.CategoryDto;
import com.kioga.kioga_api_rest.dto.category.CategoryWithProductsDto;
import com.kioga.kioga_api_rest.entites.Category;
import com.kioga.kioga_api_rest.entites.Product;
import com.kioga.kioga_api_rest.mappers.CategoryMapper;
import com.kioga.kioga_api_rest.repositories.CategoryRepository;
import com.kioga.kioga_api_rest.repositories.ProductRepository;
import com.kioga.kioga_api_rest.services.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
  private final CategoryRepository categoryRepository;
  private final CategoryMapper categoryMapper;

  private final ProductRepository productRepository;

  @Override
  public List<CategoryDto> getAllCategories() {
    return categoryMapper.toDto(categoryRepository.findAll());
  }

  @Override
  public List<CategoryWithProductsDto> getTopCategories() {
    List<Category> categories = categoryRepository.findTopCategories(Pageable.ofSize(3));

    for (Category category : categories) {
      List<Product> products = productRepository.findActiveProductsByCategory(category.getId(), Pageable.ofSize(10));
      category.setProducts(products);
    }

    return categoryMapper.toWithProductsDto(categories);
  }
}
