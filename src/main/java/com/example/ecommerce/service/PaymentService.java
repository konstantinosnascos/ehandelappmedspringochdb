package com.example.ecommerce.service;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.Payment;
import com.example.ecommerce.model.PaymentMethod;
import com.example.ecommerce.model.PaymentStatus;
import com.example.ecommerce.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Random;

@Service
public class PaymentService
{

    //Simulera betalning (90% godkänd)

    //Processa betalning
    //Hämta betalning för order

    private final PaymentRepository paymentRepository;
    private final Random random = new Random();

    public PaymentService(PaymentRepository paymentRepository)
    {
        this.paymentRepository = paymentRepository;
    }

    public boolean simulatedPayment()
    {
        return random.nextInt(10) < 9;
    }

    public Payment processingPayment(Order order, PaymentMethod method)
    {
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setMethod(method);

        boolean approved = simulatedPayment();
        if(approved)
        {
            payment.setStatus(PaymentStatus.APPROVED);
        }
        else
        {
            payment.setStatus(PaymentStatus.DECLINED);
        }
        return paymentRepository.save(payment);
    }

    public Optional<Payment> getPaymentByOrder(Order order)
    {
        return paymentRepository.findByOrder(order);
    }
}
