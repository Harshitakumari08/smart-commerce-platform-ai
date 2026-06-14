package com.smartcommerce.service.impl;

import com.smartcommerce.dto.request.ProductRequest;
import com.smartcommerce.dto.response.ProductResponse;
import com.smartcommerce.entity.Category;
import com.smartcommerce.entity.Product;
import com.smartcommerce.exception.ConflictException;
import com.smartcommerce.exception.ResourceNotFoundException;
import com.smartcommerce.repository.CategoryRepository;
import com.smartcommerce.repository.ProductRepository;
import com.smartcommerce.repository.ProductSpecifications;
import com.smartcommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    private String generateSlug(String name) {
        return name.trim().toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + request.categoryId()));

        boolean skuExists = productRepository.exists(Specification.where((root, query, cb) -> cb.equal(root.get("sku"), request.sku().trim())));
        if (skuExists) {
            throw new ConflictException("Product SKU already exists");
        }

        String name = request.name().trim();
        final String calculatedSlug;
        if (request.slug() == null || request.slug().trim().isEmpty()) {
            calculatedSlug = generateSlug(name);
        } else {
            calculatedSlug = generateSlug(request.slug());
        }

        boolean slugExists = productRepository.exists(Specification.where((root, query, cb) -> cb.equal(root.get("slug"), calculatedSlug)));
        if (slugExists) {
            throw new ConflictException("Product slug already exists");
        }

        Product product = Product.builder()
                .category(category)
                .name(name)
                .slug(calculatedSlug)
                .shortDescription(request.shortDescription())
                .description(request.description())
                .sku(request.sku().trim())
                .price(request.price())
                .comparePrice(request.comparePrice())
                .stockQuantity(request.stockQuantity())
                .brand(request.brand())
                .imageUrl(request.imageUrl())
                .thumbnailUrl(request.thumbnailUrl() != null ? request.thumbnailUrl() : request.imageUrl())
                .rating(0.0)
                .reviewCount(0)
                .isActive(true)
                .isFeatured(request.isFeatured())
                .build();

        Product saved = productRepository.save(product);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(String id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + request.categoryId()));

        boolean skuConflict = productRepository.exists(Specification.where((root, query, cb) -> 
            cb.and(
                cb.equal(root.get("sku"), request.sku().trim()),
                cb.notEqual(root.get("id"), id)
            )
        ));
        if (skuConflict) {
            throw new ConflictException("Product SKU already exists");
        }

        String name = request.name().trim();
        final String calculatedSlug;
        if (request.slug() == null || request.slug().trim().isEmpty()) {
            calculatedSlug = generateSlug(name);
        } else {
            calculatedSlug = generateSlug(request.slug());
        }

        boolean slugConflict = productRepository.exists(Specification.where((root, query, cb) -> 
            cb.and(
                cb.equal(root.get("slug"), calculatedSlug),
                cb.notEqual(root.get("id"), id)
            )
        ));
        if (slugConflict) {
            throw new ConflictException("Product slug already exists");
        }

        product.setCategory(category);
        product.setName(name);
        product.setSlug(calculatedSlug);
        product.setShortDescription(request.shortDescription());
        product.setDescription(request.description());
        product.setSku(request.sku().trim());
        product.setPrice(request.price());
        product.setComparePrice(request.comparePrice());
        product.setStockQuantity(request.stockQuantity());
        product.setBrand(request.brand());
        product.setImageUrl(request.imageUrl());
        product.setThumbnailUrl(request.thumbnailUrl() != null ? request.thumbnailUrl() : request.imageUrl());
        product.setActive(request.isActive());
        product.setFeatured(request.isFeatured());

        Product updated = productRepository.save(product);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteProduct(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        product.setActive(false);
        productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProduct(String slug) {
        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with slug: " + slug));
        return mapToResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProducts(String search, String category, Double minPrice, Double maxPrice, String sort, int page, int size) {
        Specification<Product> spec = Specification.where(ProductSpecifications.isActive());

        if (search != null && !search.trim().isEmpty()) {
            spec = spec.and(ProductSpecifications.hasSearch(search));
        }
        if (category != null && !category.trim().isEmpty()) {
            spec = spec.and(ProductSpecifications.hasCategory(category));
        }
        if (minPrice != null || maxPrice != null) {
            spec = spec.and(ProductSpecifications.hasPriceRange(minPrice, maxPrice));
        }

        Sort sortObj;
        if (sort != null) {
            sortObj = switch (sort.toLowerCase()) {
                case "price_asc" -> Sort.by(Sort.Direction.ASC, "price");
                case "price_desc" -> Sort.by(Sort.Direction.DESC, "price");
                case "newest" -> Sort.by(Sort.Direction.DESC, "createdAt");
                case "rating" -> Sort.by(Sort.Direction.DESC, "rating");
                default -> Sort.by(Sort.Direction.DESC, "createdAt");
            };
        } else {
            sortObj = Sort.by(Sort.Direction.DESC, "createdAt");
        }

        Pageable pageable = PageRequest.of(page, size, sortObj);
        return productRepository.findAll(spec, pageable).map(this::mapToResponse);
    }

    private ProductResponse mapToResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getSlug(),
                product.getShortDescription(),
                product.getDescription(),
                product.getSku(),
                product.getPrice(),
                product.getComparePrice(),
                product.getStockQuantity(),
                product.getBrand(),
                product.getImageUrl(),
                product.getThumbnailUrl(),
                product.getRating(),
                product.getReviewCount(),
                product.isActive(),
                product.isFeatured(),
                product.getCategory().getName(),
                product.getCategory().getId()
        );
    }
}
