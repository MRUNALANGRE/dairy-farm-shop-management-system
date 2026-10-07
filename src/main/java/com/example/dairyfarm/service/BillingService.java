package com.example.dairyfarm.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dairyfarm.entity.Order;
import com.example.dairyfarm.entity.OrderItem;
import com.example.dairyfarm.entity.Product;
import com.example.dairyfarm.repository.OrderItemRepository;
import com.example.dairyfarm.repository.OrderRepository;
import com.example.dairyfarm.repository.ProductRepository;

@Service
public class BillingService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    public BillingService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            ProductRepository productRepository) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Order createOrder(
            String customerName,
            String customerMobile,
            String paymentMethod,
            List<OrderItem> items) {

        double totalAmount = 0;

        for (OrderItem item : items) {
            totalAmount += item.getTotal();
        }

        Order order = new Order();

        order.setInvoiceNumber(
                "INV-" + LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
        );

        order.setCustomerName(customerName);
        order.setCustomerMobile(customerMobile);
        order.setPaymentMethod(paymentMethod);
        order.setTotalAmount(totalAmount);
        order.setOrderDate(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        for (OrderItem item : items) {

            Product product = productRepository
                    .findById(item.getProductId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Product not found: "
                                    + item.getProductId()
                            ));

            if (item.getQuantity() > product.getQuantity()) {
                throw new RuntimeException(
                        "Not enough stock for product: "
                        + product.getProductName()
                );
            }

            item.setOrderId(savedOrder.getId());
            item.setProductName(product.getProductName());
            item.setPrice(product.getPrice());

            item.setTotal(
                    product.getPrice() * item.getQuantity()
            );

            product.setQuantity(
                    product.getQuantity()
                            - item.getQuantity()
            );

            productRepository.save(product);
            orderItemRepository.save(item);
        }

        return savedOrder;
    }

    public List<Order> getAllOrders() {

        return orderRepository
                .findAllByOrderByOrderDateDesc();
    }

    public Order getOrderById(Long id) {

        return orderRepository
                .findById(id)
                .orElse(null);
    }

    public List<OrderItem> getOrderItems(Long orderId) {

        return orderItemRepository
                .findByOrderId(orderId);
    }

    // NEW METHOD FOR DATE-WISE SALES REPORT
    public List<Order> getOrdersBetweenDates(
            LocalDateTime startDate,
            LocalDateTime endDate) {

        return orderRepository
                .findByOrderDateBetweenOrderByOrderDateDesc(
                        startDate,
                        endDate
                );
    }
}