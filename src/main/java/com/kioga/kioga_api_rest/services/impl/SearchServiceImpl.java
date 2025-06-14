package com.kioga.kioga_api_rest.services.impl;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kioga.kioga_api_rest.dto.SearchBasicDto;
import com.kioga.kioga_api_rest.entities.Brand;
import com.kioga.kioga_api_rest.entities.Category;
import com.kioga.kioga_api_rest.entities.Product;
import com.kioga.kioga_api_rest.mappers.BrandMapper;
import com.kioga.kioga_api_rest.mappers.CategoryMapper;
import com.kioga.kioga_api_rest.mappers.ProductMapper;
import com.kioga.kioga_api_rest.repositories.BrandRepository;
import com.kioga.kioga_api_rest.repositories.CategoryRepository;
import com.kioga.kioga_api_rest.repositories.ProductRepository;
import com.kioga.kioga_api_rest.services.SearchService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
  private final ProductRepository productRepository;
  private final BrandRepository brandRepository;
  private final CategoryRepository categoryRepository;

  private final ProductMapper productMapper;
  private final CategoryMapper categoryMapper;
  private final BrandMapper brandMapper;

  @Override
  public SearchBasicDto getEntitiesResults(String search) {
    Pageable pageable = Pageable.ofSize(3);

    List<Product> products = productRepository.searchProducts(search, pageable);
    List<Brand> brands = brandRepository.searchBrands(search, pageable);
    List<Category> categories = categoryRepository.searchCategories(search, pageable);

    return new SearchBasicDto(
        productMapper.toBasicDto(products),
        categoryMapper.toDto(categories),
        brandMapper.toDto(brands));
  }
}
