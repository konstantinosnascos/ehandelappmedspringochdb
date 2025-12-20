package com.example.ecommerce.Repository;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    @Test
    void shouldSaveAndFindProductBySku() {

        // given
        Product product = new Product("SKU-1", "Coca cola", "Coca light", 2.99);

        // when
        repository.save(product);

        // then
        var found = repository.findBySku("SKU-123");

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test Product");
        assertThat(found.get().isActive()).isTrue();          // @PrePersist
        assertThat(found.get().getCreatedAt()).isNotNull();   // @PrePersist
    }
}
