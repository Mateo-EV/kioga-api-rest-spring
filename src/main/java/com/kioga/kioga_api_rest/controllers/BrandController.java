package com.kioga.kioga_api_rest.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kioga.kioga_api_rest.dto.BrandDto;
import com.kioga.kioga_api_rest.services.BrandService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController {
  private final BrandService brandService;

  @GetMapping
  public List<BrandDto> getAllBrands() {
    return brandService.getAllBrands();
  }

}
