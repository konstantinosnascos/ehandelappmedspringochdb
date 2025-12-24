package com.example.ecommerce.service;

import com.example.ecommerce.model.*;
import com.example.ecommerce.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTests {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    private Order order;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer("test@email.se", "Test User");
        order = new Order();
        order.setCustomer(customer);
        order.setStatus(OrderStatus.NEW);
        order.setTotal(BigDecimal.valueOf(500.00));
    }

    @Test
    void shouldProcessPaymentWithCardMethod() {
        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);

        Payment result = paymentService.processingPayment(order, PaymentMethod.CARD);

        assertThat(result).isNotNull();
        assertThat(result.getOrder()).isEqualTo(order);
        assertThat(result.getMethod()).isEqualTo(PaymentMethod.CARD);
        assertThat(result.getStatus()).isIn(PaymentStatus.APPROVED, PaymentStatus.DECLINED);

        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void shouldProcessPaymentWithInvoiceMethod() {
        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);

        Payment result = paymentService.processingPayment(order, PaymentMethod.INVOICE);

        assertThat(result).isNotNull();
        assertThat(result.getMethod()).isEqualTo(PaymentMethod.INVOICE);
        assertThat(result.getStatus()).isIn(PaymentStatus.APPROVED, PaymentStatus.DECLINED);
    }

    @Test
    void shouldSimulatePaymentApprovalRate() {
        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);

        int approvedCount = 0;
        int totalAttempts = 1000;

        for (int i = 0; i < totalAttempts; i++) {
            Payment payment = paymentService.processingPayment(order, PaymentMethod.CARD);
            if (payment.getStatus() == PaymentStatus.APPROVED) {
                approvedCount++;
            }
        }

        double approvalRate = (double) approvedCount / totalAttempts;

        // Ska vara ~90% approval rate (mellan 85% och 95% för att tillåta varians)
        assertThat(approvalRate).isBetween(0.85, 0.95);
    }

    @Test
    void shouldGetPaymentByOrder() {
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setMethod(PaymentMethod.CARD);
        payment.setStatus(PaymentStatus.APPROVED);

        when(paymentRepository.findByOrder(order)).thenReturn(Optional.of(payment));

        Optional<Payment> result = paymentService.getPaymentByOrder(order);

        assertThat(result).isPresent();
        assertThat(result.get().getOrder()).isEqualTo(order);
        assertThat(result.get().getStatus()).isEqualTo(PaymentStatus.APPROVED);
    }

    @Test
    void shouldReturnEmptyWhenNoPaymentFound() {
        when(paymentRepository.findByOrder(order)).thenReturn(Optional.empty());

        Optional<Payment> result = paymentService.getPaymentByOrder(order);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldSavePaymentCorrectly() {
        Payment expectedPayment = new Payment();
        expectedPayment.setOrder(order);
        expectedPayment.setMethod(PaymentMethod.CARD);
        expectedPayment.setStatus(PaymentStatus.APPROVED);

        when(paymentRepository.save(any(Payment.class))).thenReturn(expectedPayment);

        Payment result = paymentService.processingPayment(order, PaymentMethod.CARD);

        verify(paymentRepository).save(any(Payment.class));
        assertThat(result.getOrder()).isEqualTo(order);
    }
}