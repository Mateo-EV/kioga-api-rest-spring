package com.kioga.kioga_api_rest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kioga.kioga_api_rest.entities.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long> {

}
