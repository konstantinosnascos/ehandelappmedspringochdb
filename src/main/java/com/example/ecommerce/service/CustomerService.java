package com.example.ecommerce.service;

import com.example.ecommerce.model.Customer;
import com.example.ecommerce.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(String email, String name) {

        // enkel affärsregel
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email får inte vara tom");
        }

        if(!email.contains("@") || !email.contains(".")){
            throw new IllegalArgumentException("Ogiltigt email-format");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Namn får inte vara tomt");
        }

        if(name.length() < 2){
            throw new IllegalArgumentException("Namn måste vara minst 2 tecken");
        }

        if(!name.matches("[a-zA-ZåäöÅÄÖ ]+")) {
            throw new IllegalArgumentException("Namn får endast innehålla bokstäver");
        }

        return customerRepository
                .findByEmail(email)
                .orElseGet(() -> {
                    Customer customer = new Customer(email, name);
                    return customerRepository.save(customer);
                });
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

}
