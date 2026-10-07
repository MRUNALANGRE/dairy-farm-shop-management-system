package com.example.dairyfarm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.dairyfarm.entity.User;
import com.example.dairyfarm.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // =========================
    // SIGNUP PAGE
    // =========================

    @GetMapping("/signup")
    public String showSignupPage() {
        return "signup";
    }

    // =========================
    // PROCESS SIGNUP
    // =========================

    @PostMapping("/signup")
    public String signup(
            @RequestParam("fullName") String fullName,
            @RequestParam("email") String email,
            @RequestParam("mobile") String mobile,
            @RequestParam("password") String password,
            Model model) {

        User user = new User();

        user.setFullName(fullName);
        user.setEmail(email);
        user.setMobile(mobile);
        user.setPassword(password);

        boolean registered = userService.registerUser(user);

        if (!registered) {

            model.addAttribute(
                    "error",
                    "This email is already registered. Please login."
            );

            return "user-login";
        }

        return "redirect:/user-login";
    }

    // =========================
    // USER LOGIN PAGE
    // =========================

    @GetMapping("/user-login")
    public String showUserLoginPage() {
        return "user-login";
    }

    // =========================
    // USER LOGIN
    // =========================

    @PostMapping("/user-login")
    public String userLogin(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {

        User user = userService.login(email, password);

        if (user != null) {

            session.setAttribute("user", user);

            return "redirect:/dashboard";

        } else {

            model.addAttribute(
                    "error",
                    "Invalid email or password"
            );

            return "user-login";
        }
    }

    // =========================
    // USER DASHBOARD
    // =========================

    @GetMapping("/user-dashboard")
    public String userDashboard(HttpSession session) {

        if (session.getAttribute("user") == null) {
            return "redirect:/user-login";
        }

        return "user-dashboard";
    }

    // =========================
    // USER LOGOUT
    // =========================

    @GetMapping("/user-logout")
    public String userLogout(HttpSession session) {

        session.invalidate();

        return "redirect:/user-login";
    }
}