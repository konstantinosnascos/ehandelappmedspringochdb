package com.example.ecommerce.repository;

import com.example.ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>
{
    List<Object[]> findTopSellingProducts(int x);

    BigDecimal calculateRevenueBetween(LocalDateTime start, LocalDateTime end);
}
