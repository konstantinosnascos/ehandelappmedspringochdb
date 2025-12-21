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

    public Customer createCustomer(String email, String name)
    {
        if(email == null || email.isBlank())
        {
            throw new IllegalArgumentException("Email får inte vara tom!");
        }
        if(name == null || name.isBlank())
        {
            throw new IllegalArgumentException("Namn får inte vara tomt!");
        }

        return customerRepository.findByEmail(email).orElseGet(() -> {
            Customer customer = new Customer(email, name);
            return customerRepository.save(customer);
        });
    }



}
