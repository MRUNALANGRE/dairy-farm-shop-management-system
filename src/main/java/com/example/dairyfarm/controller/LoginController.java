package com.example.dairyfarm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.dairyfarm.entity.Admin;
import com.example.dairyfarm.service.LoginService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    // =========================
    // STARTING PAGE
    // =========================

    @GetMapping("/")
    public String showStartingPage() {
        return "signup";
    }

    // =========================
    // ADMIN LOGIN PAGE
    // =========================

    @GetMapping("/admin-login")
    public String showAdminLoginPage() {
        return "login";
    }

    // =========================
    // ADMIN LOGIN
    // =========================

    @PostMapping("/login")
    public String login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {

        Admin admin = loginService.login(username, password);

        if (admin != null) {

            session.setAttribute("admin", admin);

            return "redirect:/dashboard";

        } else {

            model.addAttribute(
                    "error",
                    "Invalid username or password"
            );

            return "login";
        }
    }

    // =========================
    // MAIN MANAGEMENT DASHBOARD
    // =========================

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {

        if (session.getAttribute("admin") != null) {
            return "dashboard";
        }

        if (session.getAttribute("user") != null) {
            return "dashboard";
        }

        return "redirect:/";
    }

    // =========================
    // LOGOUT
    // =========================

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        boolean isUser =
                session.getAttribute("user") != null;

        session.invalidate();

        if (isUser) {
            return "redirect:/user-login";
        }

        return "redirect:/";
    }
}