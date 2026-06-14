package com.smartcommerce.service.impl;

import com.smartcommerce.dto.request.CategoryRequest;
import com.smartcommerce.dto.response.CategoryResponse;
import com.smartcommerce.entity.Category;
import com.smartcommerce.exception.ConflictException;
import com.smartcommerce.exception.ResourceNotFoundException;
import com.smartcommerce.repository.CategoryRepository;
import com.smartcommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private String generateSlug(String name) {
        return name.trim().toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        String name = request.name().trim();
        if (categoryRepository.existsByName(name)) {
            throw new ConflictException("Category name already exists");
        }

        String slug = request.slug();
        if (slug == null || slug.trim().isEmpty()) {
            slug = generateSlug(name);
        } else {
            slug = generateSlug(slug);
        }

        if (categoryRepository.existsBySlug(slug)) {
            throw new ConflictException("Category slug already exists");
        }

        Category category = Category.builder()
                .name(name)
                .slug(slug)
                .description(request.description())
                .imageUrl(request.imageUrl())
                .isActive(true)
                .build();

        Category saved = categoryRepository.save(category);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(String id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        String name = request.name().trim();
        if (!category.getName().equalsIgnoreCase(name) && categoryRepository.existsByName(name)) {
            throw new ConflictException("Category name already exists");
        }

        String slug = request.slug();
        if (slug == null || slug.trim().isEmpty()) {
            slug = generateSlug(name);
        } else {
            slug = generateSlug(slug);
        }

        if (!category.getSlug().equals(slug) && categoryRepository.existsBySlug(slug)) {
            throw new ConflictException("Category slug already exists");
        }

        category.setName(name);
        category.setSlug(slug);
        category.setDescription(request.description());
        category.setImageUrl(request.imageUrl());
        category.setActive(request.isActive());

        Category updated = categoryRepository.save(category);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteCategory(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        category.setActive(false); // Soft delete
        categoryRepository.save(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategories() {
        return categoryRepository.findAll().stream()
                .filter(Category::isActive)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private CategoryResponse mapToResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getSlug(),
                category.getDescription(),
                category.getImageUrl(),
                category.isActive()
        );
    }
}
