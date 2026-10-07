package com.example.dairyfarm.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.dairyfarm.entity.Order;
import com.example.dairyfarm.entity.OrderItem;
import com.example.dairyfarm.entity.Product;
import com.example.dairyfarm.service.BillingService;
import com.example.dairyfarm.service.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller
public class BillingController {

    private final BillingService billingService;
    private final ProductService productService;

    public BillingController(
            BillingService billingService,
            ProductService productService) {

        this.billingService = billingService;
        this.productService = productService;
    }

    // =========================
    // BILLING PAGE
    // =========================

    @GetMapping("/billing")
    public String billingPage(
            Model model,
            HttpSession session) {

        if (!isLoggedIn(session)) {
            return "redirect:/";
        }

        model.addAttribute(
                "products",
                productService.getAllProducts()
        );

        return "billing/billing";
    }

    // =========================
    // CREATE BILL
    // =========================

    @PostMapping("/billing/create")
    public String createBill(
            @RequestParam("customerName") String customerName,
            @RequestParam("customerMobile") String customerMobile,
            @RequestParam("paymentMethod") String paymentMethod,
            @RequestParam("productIds") List<Long> productIds,
            @RequestParam("quantities") List<Integer> quantities,
            HttpSession session) {

        if (!isLoggedIn(session)) {
            return "redirect:/";
        }

        List<OrderItem> items = new ArrayList<>();

        for (int i = 0; i < productIds.size(); i++) {

            Long productId = productIds.get(i);
            Integer quantity = quantities.get(i);

            if (quantity == null || quantity <= 0) {
                continue;
            }

            Product product =
                    productService.getProductById(productId);

            if (product == null) {
                continue;
            }

            OrderItem item = new OrderItem();

            item.setProductId(productId);
            item.setProductName(product.getProductName());
            item.setQuantity(quantity);
            item.setPrice(product.getPrice());
            item.setTotal(
                    product.getPrice() * quantity
            );

            items.add(item);
        }

        if (items.isEmpty()) {
            return "redirect:/billing?error=empty";
        }

        Order order =
                billingService.createOrder(
                        customerName,
                        customerMobile,
                        paymentMethod,
                        items
                );

        return "redirect:/billing/invoice/"
                + order.getId();
    }

    // =========================
    // VIEW INVOICE
    // =========================

    @GetMapping("/billing/invoice/{id}")
    public String showInvoice(
            @PathVariable Long id,
            Model model,
            HttpSession session) {

        if (!isLoggedIn(session)) {
            return "redirect:/";
        }

        Order order =
                billingService.getOrderById(id);

        if (order == null) {
            return "redirect:/billing";
        }

        List<OrderItem> items =
                billingService.getOrderItems(id);

        model.addAttribute("order", order);
        model.addAttribute("items", items);

        return "billing/invoice";
    }

    // =========================
    // INVOICE HISTORY
    // =========================

    @GetMapping("/billing/orders")
    public String showOrders(
            Model model,
            HttpSession session) {

        if (!isLoggedIn(session)) {
            return "redirect:/";
        }

        model.addAttribute(
                "orders",
                billingService.getAllOrders()
        );

        return "billing/orders";
    }

    // =========================
    // LOGIN CHECK
    // =========================

    private boolean isLoggedIn(HttpSession session) {

        return session.getAttribute("admin") != null
                || session.getAttribute("user") != null;
    }
}