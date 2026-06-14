package com.smartcommerce.controller;

import com.smartcommerce.dto.request.CategoryRequest;
import com.smartcommerce.dto.response.ApiSuccessResponse;
import com.smartcommerce.dto.response.CategoryResponse;
import com.smartcommerce.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Category management endpoints")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/api/v1/categories")
    public List<CategoryResponse> list() {
        return categoryService.getCategories();
    }

    @PostMapping("/api/v1/admin/categories")
    public CategoryResponse create(@Valid @RequestBody CategoryRequest request) {
        return categoryService.createCategory(request);
    }

    @PutMapping("/api/v1/admin/categories/{id}")
    public CategoryResponse update(@PathVariable String id, @Valid @RequestBody CategoryRequest request) {
        return categoryService.updateCategory(id, request);
    }

    @DeleteMapping("/api/v1/admin/categories/{id}")
    public ApiSuccessResponse<Void> delete(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return ApiSuccessResponse.success("Category deleted successfully");
    }
}
