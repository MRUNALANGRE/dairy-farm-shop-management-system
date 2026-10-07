package com.example.dairyfarm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dairyfarm.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {

}