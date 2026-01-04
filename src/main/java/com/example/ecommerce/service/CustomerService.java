package com.example.ecommerce.service;

import com.example.ecommerce.model.Customer;
import com.example.ecommerce.repository.CustomerRepository;
import com.example.ecommerce.repository.CustomerUnsafeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerUnsafeRepository unsafeRepository;

    public CustomerService(CustomerRepository customerRepository, CustomerUnsafeRepository unsafeRepository) {
        this.customerRepository = customerRepository;
        this.unsafeRepository = unsafeRepository;
    }

    public Customer createCustomer(String email, String name) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email får inte vara tom");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Namn får inte vara tomt");
        }

        return customerRepository.findByEmail(email)
                .orElseGet(() -> customerRepository.save(new Customer(email, name)));
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public void updateCustomer(String email, String newName) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Kund med email " + email + " hittades inte."));

        if (newName != null && !newName.isBlank()) {
            customer.setName(newName);
            customerRepository.save(customer);
        }
    }

    public void deleteCustomer(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Kund med email " + email + " hittades inte."));
        customerRepository.delete(customer);
    }

    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public Optional<Customer> findByEmailUnsafe(String email) {
        return unsafeRepository.findByEmailUnsafe(email);
    }
}