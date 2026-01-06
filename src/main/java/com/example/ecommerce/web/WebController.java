package com.example.ecommerce.web;

import com.example.ecommerce.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import com.example.ecommerce.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WebController {

    private final CustomerService customerService;

    public WebController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/")
    public String home(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        Page<Customer> customers =
                customerService.getAllCustomers(
                        PageRequest.of(page, size)
                );

        model.addAttribute("customers", customers);

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

    @PostMapping("/customers/delete")
    public String deleteCustomer(@RequestParam String email, RedirectAttributes ra) {
        try {
            customerService.deleteCustomer(email);
            ra.addFlashAttribute("message", "Kunden togs bort.");
        } catch (DataIntegrityViolationException e) {
            ra.addFlashAttribute("error", "Kunden kan inte tas bort eftersom den har ordrar.");
        }
        return "redirect:/";
    }

    @GetMapping("/customers/edit")
    public String editCustomer(
            @RequestParam String email,
            Model model
    ) {
        Customer customer = customerService.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Kund finns inte"));
        model.addAttribute("customer", customer);
        return "edit-customer";
    }

    @PostMapping("/customers/update")
    public String updateCustomer(
            @RequestParam String email,
            @RequestParam String name
    ) {
        customerService.updateCustomer(email, name);
        return "redirect:/";
    }

    @PostMapping("/select-customer")
    public String selectCustomer(
            @RequestParam String email,
            HttpSession session
    ) {
        session.setAttribute("customerEmail", email);
        return "redirect:/products";
    }
}