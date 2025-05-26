package com.kioga.kioga_api_rest.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kioga.kioga_api_rest.dto.SearchBasicDto;
import com.kioga.kioga_api_rest.services.SearchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {
  private final SearchService searchService;

  @GetMapping("/{search}")
  public SearchBasicDto getEntitiesResults(@PathVariable String search) {
    return searchService.getEntitiesResults(search);
  }

}
