package com.smartcommerce.dto.response;

import java.math.BigDecimal;

public record ProductResponse(
        String id,
        String name,
        String slug,
        String shortDescription,
        String description,
        String sku,
        BigDecimal price,
        BigDecimal comparePrice,
        int stockQuantity,
        String brand,
        String imageUrl,
        String thumbnailUrl,
        Double rating,
        int reviewCount,
        boolean isActive,
        boolean isFeatured,
        String category,
        String categoryId
) {}
