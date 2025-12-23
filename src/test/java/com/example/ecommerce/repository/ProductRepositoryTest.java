package com.example.ecommerce.repository;

import com.example.ecommerce.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
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
        var found = repository.findBySku("SKU-1");

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Coca cola");
        assertThat(found.get().isActive()).isTrue();          // @PrePersist
        assertThat(found.get().getCreatedAt()).isNotNull();   // @PrePersist
    }
}
