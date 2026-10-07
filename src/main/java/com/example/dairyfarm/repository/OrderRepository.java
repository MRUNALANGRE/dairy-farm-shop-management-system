package com.example.dairyfarm.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dairyfarm.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByOrderByOrderDateDesc();

    List<Order> findByOrderDateBetweenOrderByOrderDateDesc(
            LocalDateTime startDate,
            LocalDateTime endDate
    );
}