package com.example.ecommerce.service;

import com.example.ecommerce.model.Customer;
import com.example.ecommerce.repository.CustomerRepository;
import com.example.ecommerce.exception.CustomerNotFoundException;
import com.example.ecommerce.exception.DuplicateEmailException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> listAll() {
        return customerRepository.findAll();
    }

    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public Customer create(String email, String name) {
        if (customerRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("Kund med email " + email + " finns redan");
        }
        return customerRepository.save(new Customer(email, name));
    }

    public Customer update(Long id, String newName) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Kund med id " + id + " finns inte"));
        customer.setName(newName);
        return customerRepository.save(customer);
    }
}
//CRUD kunder

//Hämta alla kunder
//Hämta kund med id
//Hämta kund med email
//Skapa ny kund
//Uppdatera kund
//kan behövas fler, men så folk har en ide av vad som behöver göras i klassen


