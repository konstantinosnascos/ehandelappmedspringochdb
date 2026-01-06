package com.example.ecommerce.repository;

import com.example.ecommerce.dto.TopProductDTO;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>
{
    @Query("""
    select new com.example.ecommerce.dto.TopProductDTO(
        p.name,
        sum(oi.qty)
    )
    from OrderItem oi
    join oi.product p
    group by p.name
    order by sum(oi.qty) desc
""")
    List<TopProductDTO> findTopSellingProducts(Pageable pageable);

    List<Order> findByCustomer(Customer customer);

    @Query("SELECT COALESCE(SUM(o.total), 0) FROM Order o WHERE o.status = 'PAID' AND o.createdAt BETWEEN :start AND :end")
    BigDecimal calculateRevenueBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
