package com.example.dairyfarm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dairyfarm.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}