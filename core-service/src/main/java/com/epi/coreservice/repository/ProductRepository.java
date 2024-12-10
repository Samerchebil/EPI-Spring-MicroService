package com.epi.coreservice.repository;

import com.epi.coreservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> getProductsByCategoryId(String id);

    List<Product> getProductsByKeysContainsIgnoreCase(String key);
}
