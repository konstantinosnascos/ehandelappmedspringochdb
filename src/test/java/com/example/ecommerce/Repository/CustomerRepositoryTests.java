package com.example.ecommerce.repository;

import com.example.ecommerce.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application.properties")
class CustomerRepositoryTests {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void shouldSaveAndFindCustomerById() {
        Customer customer = new Customer("test-unique@email.se", "Test Person");

        Customer saved = customerRepository.save(customer);
        Optional<Customer> found = customerRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test-unique@email.se");
    }

    @Test
    void shouldFindCustomerByEmail() {
        Customer customer = new Customer("find-test@email.se", "Anna Andersson");
        customerRepository.save(customer);

        Optional<Customer> found = customerRepository.findByEmail("find-test@email.se");

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Anna Andersson");
    }

    @Test
    void shouldReturnEmptyWhenEmailNotFound() {
        Optional<Customer> found = customerRepository.findByEmail("finns.inte.ansen@email.se");
        assertThat(found).isEmpty();
    }

    @Test
    void shouldCheckIfEmailExists() {
        customerRepository.save(new Customer("exists-check@email.se", "Existing User"));

        boolean exists = customerRepository.existsByEmail("exists-check@email.se");
        boolean notExists = customerRepository.existsByEmail("not.exists.ansen@email.se");

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    void shouldUpdateCustomer() {
        Customer customer = customerRepository.save(new Customer("update-test@email.se", "Gammalt Namn"));

        customer.setName("Nytt Namn");
        customerRepository.save(customer);

        Customer updated = customerRepository.findById(customer.getId()).orElseThrow();
        assertThat(updated.getName()).isEqualTo("Nytt Namn");
    }

    @Test
    void shouldReturnEmptyWhenCustomerNotFound() {
        Optional<Customer> result = customerRepository.findById(99999L);
        assertThat(result).isEmpty();
    }
}


