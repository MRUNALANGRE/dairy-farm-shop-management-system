package com.example.dairyfarm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.dairyfarm.entity.Company;
import com.example.dairyfarm.service.CompanyService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    // =========================
    // MANAGE COMPANIES
    // =========================

    @GetMapping("/companies")
    public String showCompanies(
            Model model,
            HttpSession session) {

        if (!isLoggedIn(session)) {
            return "redirect:/";
        }

        model.addAttribute(
                "companies",
                companyService.getAllCompanies()
        );

        return "company/manage-companies";
    }

    // =========================
    // ADD COMPANY PAGE
    // =========================

    @GetMapping("/companies/add")
    public String showAddCompany(
            Model model,
            HttpSession session) {

        if (!isLoggedIn(session)) {
            return "redirect:/";
        }

        model.addAttribute(
                "company",
                new Company()
        );

        return "company/add-company";
    }

    // =========================
    // SAVE COMPANY
    // =========================

    @PostMapping("/companies/save")
    public String saveCompany(
            @ModelAttribute("company") Company company,
            HttpSession session) {

        if (!isLoggedIn(session)) {
            return "redirect:/";
        }

        companyService.saveCompany(company);

        return "redirect:/companies";
    }

    // =========================
    // EDIT COMPANY
    // =========================

    @GetMapping("/companies/edit/{id}")
    public String showEditCompany(
            @PathVariable Long id,
            Model model,
            HttpSession session) {

        if (!isLoggedIn(session)) {
            return "redirect:/";
        }

        Company company =
                companyService.getCompanyById(id);

        model.addAttribute(
                "company",
                company
        );

        return "company/edit-company";
    }

    // =========================
    // DELETE COMPANY
    // =========================

    @GetMapping("/companies/delete/{id}")
    public String deleteCompany(
            @PathVariable Long id,
            HttpSession session) {

        if (!isLoggedIn(session)) {
            return "redirect:/";
        }

        companyService.deleteCompany(id);

        return "redirect:/companies";
    }

    // =========================
    // LOGIN CHECK
    // =========================

    private boolean isLoggedIn(HttpSession session) {

        return session.getAttribute("admin") != null
                || session.getAttribute("user") != null;
    }
}