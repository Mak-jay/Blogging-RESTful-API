package com.blogapi.service;

import com.blogapi.payload.CategoryRequest;
import com.blogapi.model.Category;
import com.blogapi.payload.CategoryResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {

    CategoryResponse createCategory(@Valid CategoryRequest category);

    List<CategoryResponse> getAllCategories();

    CategoryResponse getCategoryById(Long id);

    void deleteCategory(Long id);
}
