package com.example.dairyfarm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.dairyfarm.entity.Category;
import com.example.dairyfarm.service.CategoryService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // =========================
    // MANAGE CATEGORIES
    // =========================

    @GetMapping("/categories")
    public String showCategories(
            Model model,
            HttpSession session) {

        if (!isLoggedIn(session)) {
            return "redirect:/";
        }

        model.addAttribute(
                "categories",
                categoryService.getAllCategories()
        );

        return "category/manage-categories";
    }

    // =========================
    // ADD CATEGORY PAGE
    // =========================

    @GetMapping("/categories/add")
    public String showAddCategory(
            Model model,
            HttpSession session) {

        if (!isLoggedIn(session)) {
            return "redirect:/";
        }

        model.addAttribute(
                "category",
                new Category()
        );

        return "category/add-category";
    }

    // =========================
    // SAVE CATEGORY
    // =========================

    @PostMapping("/categories/save")
    public String saveCategory(
            @ModelAttribute("category") Category category,
            HttpSession session) {

        if (!isLoggedIn(session)) {
            return "redirect:/";
        }

        categoryService.saveCategory(category);

        return "redirect:/categories";
    }

    // =========================
    // EDIT CATEGORY
    // =========================

    @GetMapping("/categories/edit/{id}")
    public String showEditCategory(
            @PathVariable Long id,
            Model model,
            HttpSession session) {

        if (!isLoggedIn(session)) {
            return "redirect:/";
        }

        Category category =
                categoryService.getCategoryById(id);

        model.addAttribute(
                "category",
                category
        );

        return "category/edit-category";
    }

    // =========================
    // DELETE CATEGORY
    // =========================

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(
            @PathVariable Long id,
            HttpSession session) {

        if (!isLoggedIn(session)) {
            return "redirect:/";
        }

        categoryService.deleteCategory(id);

        return "redirect:/categories";
    }

    // =========================
    // LOGIN CHECK
    // =========================

    private boolean isLoggedIn(HttpSession session) {

        return session.getAttribute("admin") != null
                || session.getAttribute("user") != null;
    }
}