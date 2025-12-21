package com.example.ecommerce.repository;

import com.example.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long>
{

    // Hibernate genererar implementationen automatiskt från metodnamnet!
    Optional<Product> findBySku(String sku);

    List<Product> findByNameContainingIgnoreCase(String keyword);

    boolean existsBySku(String sku);

    List<Product> findByActive(boolean active);

    List<Product> findProductsWithLowStock(int threshold);

    // findAll(), findById(), save(), delete() finns redan från JpaRepository och behöver inte skapas

}
