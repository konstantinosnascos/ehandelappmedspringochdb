package com.example.ecommerce.service;

import com.example.ecommerce.menu.CustomerMenu;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer register(String)
}
