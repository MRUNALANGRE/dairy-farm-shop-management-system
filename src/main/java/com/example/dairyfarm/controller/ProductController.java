package com.example.dairyfarm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.dairyfarm.entity.Product;
import com.example.dairyfarm.service.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // =========================
    // MANAGE PRODUCTS
    // =========================

    @GetMapping("/products")
    public String showProducts(
            Model model,
            HttpSession session) {

        if (!isLoggedIn(session)) {
            return "redirect:/";
        }

        model.addAttribute(
                "products",
                productService.getAllProducts()
        );

        return "product/manage-products";
    }

    // =========================
    // ADD PRODUCT PAGE
    // =========================

    @GetMapping("/products/add")
    public String showAddProduct(
            Model model,
            HttpSession session) {

        if (!isLoggedIn(session)) {
            return "redirect:/";
        }

        model.addAttribute(
                "product",
                new Product()
        );

        return "product/add-product";
    }

    // =========================
    // SAVE PRODUCT
    // =========================

    @PostMapping("/products/save")
    public String saveProduct(
            @ModelAttribute("product") Product product,
            HttpSession session) {

        if (!isLoggedIn(session)) {
            return "redirect:/";
        }

        productService.saveProduct(product);

        return "redirect:/products";
    }

    // =========================
    // EDIT PRODUCT
    // =========================

    @GetMapping("/products/edit/{id}")
    public String showEditProduct(
            @PathVariable Long id,
            Model model,
            HttpSession session) {

        if (!isLoggedIn(session)) {
            return "redirect:/";
        }

        Product product =
                productService.getProductById(id);

        model.addAttribute(
                "product",
                product
        );

        return "product/edit-product";
    }

    // =========================
    // DELETE PRODUCT
    // =========================

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(
            @PathVariable Long id,
            HttpSession session) {

        if (!isLoggedIn(session)) {
            return "redirect:/";
        }

        productService.deleteProduct(id);

        return "redirect:/products";
    }

    // =========================
    // SEARCH PRODUCTS
    // =========================

    @GetMapping("/products/search")
    public String searchProducts(
            @RequestParam("productName") String productName,
            Model model,
            HttpSession session) {

        if (!isLoggedIn(session)) {
            return "redirect:/";
        }

        model.addAttribute(
                "products",
                productService.searchProducts(productName)
        );

        model.addAttribute(
                "searchName",
                productName
        );

        return "product/manage-products";
    }

    // =========================
    // LOGIN CHECK
    // =========================

    private boolean isLoggedIn(HttpSession session) {

        return session.getAttribute("admin") != null
                || session.getAttribute("user") != null;
    }
}