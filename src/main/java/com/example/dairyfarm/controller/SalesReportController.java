package com.example.dairyfarm.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.dairyfarm.entity.Order;
import com.example.dairyfarm.service.BillingService;

import jakarta.servlet.http.HttpSession;

@Controller
public class SalesReportController {

    private final BillingService billingService;

    public SalesReportController(BillingService billingService) {
        this.billingService = billingService;
    }

    // =========================
    // SALES REPORT
    // =========================

    @GetMapping("/sales-report")
    public String salesReport(
            Model model,
            HttpSession session) {

        if (!isLoggedIn(session)) {
            return "redirect:/";
        }

        List<Order> orders =
                billingService.getAllOrders();

        double totalSales =
                calculateTotalSales(orders);

        model.addAttribute("orders", orders);
        model.addAttribute("totalSales", totalSales);
        model.addAttribute("totalOrders", orders.size());

        return "report/sales-report";
    }

    // =========================
    // DATE-WISE SALES SEARCH
    // =========================

    @GetMapping("/sales-report/search")
    public String searchSalesReport(
            @RequestParam("fromDate") String fromDate,
            @RequestParam("toDate") String toDate,
            Model model,
            HttpSession session) {

        if (!isLoggedIn(session)) {
            return "redirect:/";
        }

        LocalDate start =
                LocalDate.parse(fromDate);

        LocalDate end =
                LocalDate.parse(toDate);

        LocalDateTime startDateTime =
                start.atStartOfDay();

        LocalDateTime endDateTime =
                end.plusDays(1)
                   .atStartOfDay()
                   .minusNanos(1);

        List<Order> orders =
                billingService.getOrdersBetweenDates(
                        startDateTime,
                        endDateTime
                );

        double totalSales =
                calculateTotalSales(orders);

        model.addAttribute("orders", orders);
        model.addAttribute("totalSales", totalSales);
        model.addAttribute("totalOrders", orders.size());

        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);

        return "report/sales-report";
    }

    // =========================
    // CALCULATE TOTAL SALES
    // =========================

    private double calculateTotalSales(
            List<Order> orders) {

        double totalSales = 0;

        for (Order order : orders) {

            if (order.getTotalAmount() != null) {
                totalSales += order.getTotalAmount();
            }
        }

        return totalSales;
    }

    // =========================
    // LOGIN CHECK
    // =========================

    private boolean isLoggedIn(HttpSession session) {

        return session.getAttribute("admin") != null
                || session.getAttribute("user") != null;
    }
}