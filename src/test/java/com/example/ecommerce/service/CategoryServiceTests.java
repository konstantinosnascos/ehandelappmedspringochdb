package com.example.ecommerce.service;

import com.example.ecommerce.model.Category;
import com.example.ecommerce.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTests {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category1;
    private Category category2;

    @BeforeEach
    void setUp() {
        category1 = new Category("Elektronik");
        category2 = new Category("Kläder");
    }

    @Test
    void shouldGetAllCategories() {
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));

        List<Category> result = categoryService.getAllCategories();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(category1, category2);
        verify(categoryRepository).findAll();
    }

    @Test
    void shouldGetCategoryById() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));

        Optional<Category> result = categoryService.getCategoriyViaId(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Elektronik");
    }

    @Test
    void shouldReturnEmptyWhenCategoryIdNotFound() {
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Category> result = categoryService.getCategoriyViaId(999L);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldGetCategoryByName() {
        when(categoryRepository.findByNameIgnoreCase("elektronik")).thenReturn(Optional.of(category1));

        Optional<Category> result = categoryService.getCategoriyViaName("elektronik");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Elektronik");
    }

    @Test
    void shouldGetCategoryByNameCaseInsensitive() {
        when(categoryRepository.findByNameIgnoreCase("ELEKTRONIK")).thenReturn(Optional.of(category1));

        Optional<Category> result = categoryService.getCategoriyViaName("ELEKTRONIK");

        assertThat(result).isPresent();
    }

    @Test
    void shouldCreateNewCategory() {
        when(categoryRepository.findByNameIgnoreCase("Sport")).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenAnswer(i -> i.getArguments()[0]);

        Category result = categoryService.createCategory("Sport");

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Sport");
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void shouldThrowExceptionWhenCreatingDuplicateCategory() {
        when(categoryRepository.findByNameIgnoreCase("Elektronik")).thenReturn(Optional.of(category1));

        assertThatThrownBy(() -> categoryService.createCategory("Elektronik"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Kategorin 'Elektronik' finns redan");

        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void shouldNotAllowDuplicateCategoryWithDifferentCase() {
        when(categoryRepository.findByNameIgnoreCase("elektronik")).thenReturn(Optional.of(category1));

        assertThatThrownBy(() -> categoryService.createCategory("elektronik"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}