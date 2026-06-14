package com.smartcommerce.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        @NotBlank String name,
        @NotBlank String slug,
        String description,
        String imageUrl,
        boolean isActive
) {}
