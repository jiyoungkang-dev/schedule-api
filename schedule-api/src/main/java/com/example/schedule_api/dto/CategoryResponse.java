package com.example.schedule_api.dto;

import com.example.schedule_api.entity.Category;

public record CategoryResponse(
        Long id,
        String name
) {
    public static CategoryResponse from(Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }
}
