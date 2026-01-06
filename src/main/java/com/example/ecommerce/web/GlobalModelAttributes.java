package com.example.ecommerce.web;

import com.example.ecommerce.model.Customer;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    private final CustomerService customerService;
    private final CartService cartService;

    public GlobalModelAttributes(CustomerService customerService,
                                 CartService cartService) {
        this.customerService = customerService;
        this.cartService = cartService;
    }

    @ModelAttribute("activeCustomer")
    public Customer activeCustomer(HttpSession session) {
        String email = (String) session.getAttribute("customerEmail");
        if (email == null) {
            return null;
        }
        return customerService.findByEmail(email).orElse(null);
    }

    @ModelAttribute("cartItemCount")
    public Integer cartItemCount(HttpSession session) {
        String email = (String) session.getAttribute("customerEmail");
        if (email == null) {
            return 0;
        }

        Customer customer = customerService.findByEmail(email).orElse(null);
        if (customer == null) {
            return 0;
        }

        return cartService.getCartWithItems(customer)
                .getItems()
                .stream()
                .mapToInt(item -> item.getQty())
                .sum();
    }
}