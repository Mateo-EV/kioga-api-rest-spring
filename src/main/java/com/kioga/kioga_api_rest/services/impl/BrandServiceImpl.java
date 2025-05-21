package com.kioga.kioga_api_rest.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kioga.kioga_api_rest.dto.BrandDto;
import com.kioga.kioga_api_rest.mappers.BrandMapper;
import com.kioga.kioga_api_rest.repositories.BrandRepository;
import com.kioga.kioga_api_rest.services.BrandService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
  private final BrandRepository brandRepository;
  private final BrandMapper brandMapper;

  @Override
  public List<BrandDto> getAllBrands() {
    return brandMapper.toDto(brandRepository.findAll());
  }
}
