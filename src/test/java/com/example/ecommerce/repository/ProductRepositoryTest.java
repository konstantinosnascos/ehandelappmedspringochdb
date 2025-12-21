package com.example.ecommerce.repository;

import com.example.ecommerce.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
