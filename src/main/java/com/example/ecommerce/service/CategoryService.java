package com.example.ecommerce.service;

import com.example.ecommerce.model.Category;
import com.example.ecommerce.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    //Hämta alla kategorier
    public List<Category> getAllCatergories() {
        return categoryRepository.findAll();
    }

    //Hämta kategori med id
    public Optional<Category> getCategoriyViaId(Long id) {
        return categoryRepository.findById(id);
    }

    //Hämta kategori med namn
    public Optional<Category> getCategoriyViaName(String name) {
        return categoryRepository.findByNameIgnoreCase(name);
    }

    //Skapa ny kategori
    public Category createCategory(String name) {
        if (categoryRepository.findByNameIgnoreCase(name).isPresent()) {
            throw new IllegalArgumentException("Kategorin '" + name + "' finns redan.");
        }

        Category createdCategory = new Category(name);
        createdCategory.setName(name);
        return categoryRepository.save(createdCategory);
    }
}
