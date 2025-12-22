package com.example.ecommerce.repository;

import com.example.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long>
{

    // Hibernate genererar implementationen automatiskt från metodnamnet!
    Optional<Product> findBySku(String sku);

    List<Product> findByNameContainingIgnoreCase(String keyword);
    List<Product> findByCategoriesNameIgnoreCase(String categoryName);

    boolean existsBySku(String sku);

    List<Product> findByActive(boolean active);

    @Query("SELECT p FROM Product p JOIN Inventory i ON p.id = i.productId WHERE i.inStock < :threshold")
    List<Product> findProductsWithLowStock(@Param("threshold") int threshold);

    // findAll(), findById(), save(), delete() finns redan från JpaRepository och behöver inte skapas

}
