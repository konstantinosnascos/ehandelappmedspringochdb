package com.example.ecommerce.web;

import com.example.ecommerce.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class WebController {

    private final CustomerService customerService;

    public WebController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("customers", customerService.getAllCustomers());
        return "index";
    }

    @PostMapping("/customers")
    public String createCustomer(
            @RequestParam String name,
            @RequestParam String email
    ) {
        customerService.createCustomer(email, name);
        return "redirect:/";
    }
}