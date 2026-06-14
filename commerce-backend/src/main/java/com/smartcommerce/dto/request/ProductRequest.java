package com.smartcommerce.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "Category ID is required") String categoryId,
        @NotBlank(message = "Product name is required") String name,
        String slug,
        String shortDescription,
        String description,
        @NotBlank(message = "SKU is required") String sku,
        @NotNull(message = "Price is required") @DecimalMin(value = "0.01", message = "Price must be greater than 0") BigDecimal price,
        BigDecimal comparePrice,
        @Min(value = 0, message = "Stock quantity cannot be negative") int stockQuantity,
        String brand,
        String imageUrl,
        String thumbnailUrl,
        boolean isActive,
        boolean isFeatured
) {}
