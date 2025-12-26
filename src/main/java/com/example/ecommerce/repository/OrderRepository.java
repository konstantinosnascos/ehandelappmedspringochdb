package com.example.ecommerce.repository;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>
{
    @Query(value = """
    SELECT p.name, SUM(oi.qty) as total_sold 
    FROM order_items oi 
    JOIN products p ON oi.product_id = p.id 
    JOIN orders o ON oi.order_id = o.id 
    WHERE o.status = 'PAID'
    GROUP BY p.id, p.name 
    ORDER BY total_sold DESC 
    LIMIT :limit
    """, nativeQuery = true)
    List<Object[]> findTopSellingProducts(@Param("limit") int limit);


    @Query("SELECT COALESCE(SUM(o.total), 0) FROM Order o WHERE o.status = 'PAID' AND o.createdAt BETWEEN :start AND :end")
    BigDecimal calculateRevenueBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);



}
