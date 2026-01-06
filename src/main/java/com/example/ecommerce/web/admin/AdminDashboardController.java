package com.example.ecommerce.web.admin;

import com.example.ecommerce.service.ReportService;
import com.example.ecommerce.service.InventoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    private final ReportService reportService;
    private final InventoryService inventoryService;

    public AdminDashboardController(ReportService reportService,
                                    InventoryService inventoryService) {
        this.reportService = reportService;
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/reports/top-products")
    public String topProducts(Model model) {
        model.addAttribute(
                "topProducts",
                reportService.getTopSellingProducts(5)
        );
        return "admin/top-products";
    }

    @GetMapping("/reports/low-stock")
    public String lowStock(
            @RequestParam(defaultValue = "5") int lt,
            Model model
    ) {
        model.addAttribute(
                "lowStockProducts",
                reportService.getLowStockProducts(lt)
        );
        model.addAttribute("threshold", lt);

        return "admin/low-stock";
    }

    @GetMapping("/reports/revenue")
    public String revenue(
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            Model model
    ) {
        if (from != null && to != null) {
            model.addAttribute(
                    "revenue",
                    reportService.getRevenueBetween(
                            LocalDate.parse(from).atStartOfDay(),
                            LocalDate.parse(to).atTime(23, 59, 59)
                    )
            );
        }
        return "admin/revenue";
    }
}