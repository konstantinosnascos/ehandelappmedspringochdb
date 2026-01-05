package com.example.ecommerce.repository;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>
{
    Optional<Payment> findByOrder(Order order);

    @Query("""
    select p from Payment p
    where p.order = :order
    order by p.timestamp desc
""")
    List<Payment> findLatestByOrder(@Param("order") Order order);
}

