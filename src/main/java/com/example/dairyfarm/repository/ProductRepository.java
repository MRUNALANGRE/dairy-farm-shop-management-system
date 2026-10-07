package com.example.dairyfarm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dairyfarm.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByProductNameContainingIgnoreCase(String productName);
}