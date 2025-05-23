package com.example.demo.repository;


import com.example.demo.Models.Enums.PhoneModel;
import com.example.demo.Models.Product;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Hidden
@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    List<Product> findByCategoryId(UUID categoryId);
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);
    List<Product> findByPhoneModel(PhoneModel phoneModel);
}
