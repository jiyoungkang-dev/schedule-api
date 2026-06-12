package com.example.schedule_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.schedule_api.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
