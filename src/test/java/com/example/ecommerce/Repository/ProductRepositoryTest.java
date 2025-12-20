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
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    @Test
    void shouldSaveAndFindProductBySku() {

        // given
        Product product = new Product(
                "SKU-123",
                "Test Product",
                "Test description",
                new BigDecimal("99.90"),
                false,      // sätt false för att testa @PrePersist
                null        // låt @PrePersist sätta createdAt
        );

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
