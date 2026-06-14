package com.smartcommerce.service;

import com.smartcommerce.dto.request.CategoryRequest;
import com.smartcommerce.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest request);
    CategoryResponse updateCategory(String id, CategoryRequest request);
    void deleteCategory(String id);
    List<CategoryResponse> getCategories();
}
