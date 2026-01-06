package com.example.ecommerce.web.admin;

import com.example.ecommerce.exception.AccessDeniedException;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.service.CustomerService;
import com.example.ecommerce.service.ReportService;
import com.example.ecommerce.service.InventoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    private final ReportService reportService;
    private final InventoryService inventoryService;
    private final CustomerService customerService;

    public AdminDashboardController(ReportService reportService,
                                    InventoryService inventoryService,
                                    CustomerService customerService) {
        this.reportService = reportService;
        this.inventoryService = inventoryService;
        this.customerService = customerService;
    }

    @GetMapping
    public String dashboard(HttpSession session) {
        requireAdmin(session);
        return "admin/dashboard";
    }

    @GetMapping("/reports/top-products")
    public String topProducts(HttpSession session, Model model) {
        requireAdmin(session);
        model.addAttribute(
                "topProducts",
                reportService.getTopSellingProducts(5)
        );
        return "admin/top-products";
    }

    @GetMapping("/reports/low-stock")
    public String lowStock(
            @RequestParam(defaultValue = "5") int lt,
            HttpSession session,
            Model model
    ) {
        requireAdmin(session);
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
            HttpSession session,
            Model model
    ) {
        requireAdmin(session);
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

    private void requireAdmin(HttpSession session)
    {
        String email = (String) session.getAttribute("customerEmail");

        if(email==null)
        {
            throw new AccessDeniedException("Ingen kund vald");
        }

        Customer customer = customerService.findByEmail(email)
                .orElseThrow(() -> new AccessDeniedException("Kund finns inte"));

        if(!customer.hasRole("ADMIN"))
        {
            throw new AccessDeniedException("Du saknar behörighet att se admin-sidor");
        }
    }
}