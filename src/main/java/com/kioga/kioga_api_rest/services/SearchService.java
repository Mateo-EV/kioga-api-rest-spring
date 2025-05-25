package com.kioga.kioga_api_rest.services;

public interface SearchService {
  /**
   * @return Expected to return this formal
   *         Object format:
   *         {
   *         products: 3 Products,
   *         categories: 3 Categories,
   *         brands: 3 Brands
   *         }
   */
  public String getEntitiesResults(String search);
}
