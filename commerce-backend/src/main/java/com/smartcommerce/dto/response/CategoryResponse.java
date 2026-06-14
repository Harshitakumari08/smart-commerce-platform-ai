package com.smartcommerce.dto.response;

public record CategoryResponse(
        String id,
        String name,
        String slug,
        String description,
        String imageUrl,
        boolean isActive
) {}
