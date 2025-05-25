package com.kioga.kioga_api_rest.services;

import java.util.List;

import com.kioga.kioga_api_rest.dto.brand.BrandDto;

public interface BrandService {
  List<BrandDto> getAllBrands();

  // BrandDto getBrandById(Long id);

  // BrandDto createBrand(BrandDto brandDto);

  // BrandDto updateBrand(Long id, BrandDto brandDto);

  // void deleteBrandById(Long id);
}
