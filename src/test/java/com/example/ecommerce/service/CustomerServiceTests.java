package com.example.ecommerce.service;

import com.example.ecommerce.model.Customer;
import com.example.ecommerce.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTests {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void shouldCreateNewCustomer() {
        when(customerRepository.findByEmail("new@email.se")).thenReturn(Optional.empty());
        when(customerRepository.save(any(Customer.class))).thenAnswer(i -> i.getArguments()[0]);

        Customer result = customerService.createCustomer("new@email.se", "New User");

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("new@email.se");
        assertThat(result.getName()).isEqualTo("New User");
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void shouldReturnExistingCustomer() {
        Customer existing = new Customer("existing@email.se", "Existing User");
        when(customerRepository.findByEmail("existing@email.se")).thenReturn(Optional.of(existing));

        Customer result = customerService.createCustomer("existing@email.se", "New Name");

        assertThat(result).isEqualTo(existing);
        assertThat(result.getName()).isEqualTo("Existing User");
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailIsNull() {
        assertThatThrownBy(() -> customerService.createCustomer(null, "Test User"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email får inte vara tom");

        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailIsBlank() {
        assertThatThrownBy(() -> customerService.createCustomer("   ", "Test User"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email får inte vara tom");
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        assertThatThrownBy(() -> customerService.createCustomer("test@email.se", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Namn får inte vara tomt");
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        assertThatThrownBy(() -> customerService.createCustomer("test@email.se", "   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Namn får inte vara tomt");
    }
}