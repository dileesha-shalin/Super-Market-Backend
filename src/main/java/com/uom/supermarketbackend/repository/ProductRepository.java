package com.uom.supermarketbackend.repository;

import com.uom.supermarketbackend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByNameContainingIgnoreCase(String productName);
}