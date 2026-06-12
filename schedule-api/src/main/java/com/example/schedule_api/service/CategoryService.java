package com.example.schedule_api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.schedule_api.dto.CategoryCreateRequest;
import com.example.schedule_api.dto.CategoryResponse;
import com.example.schedule_api.entity.Category;
import com.example.schedule_api.repository.CategoryRepository;

@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public CategoryResponse create(CategoryCreateRequest request) {
        Category category = categoryRepository.save(new Category(request.name()));
        return CategoryResponse.from(category);
    }

    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::from)
                .toList();
    }
}
