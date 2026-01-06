package com.example.ecommerce.service;

import com.example.ecommerce.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.ecommerce.repository.CustomerRepository;
import com.example.ecommerce.repository.CustomerUnsafeRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<Customer> getAllCustomersForConsole() {
        return customerRepository.findAll();
    }

    public Page<Customer> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    public void updateCustomer(String email, String newName) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Kund med email " + email + " hittades inte."));

        if (newName != null && !newName.isBlank()) {
            customer.setName(newName);
            customerRepository.save(customer);
        }
    }

    @Transactional
    public void deleteCustomer(String email) {
        try {
            customerRepository.deleteByEmail(email);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException(
                    "Kunden kan inte tas bort eftersom den har ordrar"
            );
        }
    }

    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public Optional<Customer> findByEmailUnsafe(String email) {
        return unsafeRepository.findByEmailUnsafe(email);
    }
}