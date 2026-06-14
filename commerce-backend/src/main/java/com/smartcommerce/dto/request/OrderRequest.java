package com.smartcommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderRequest(
        @NotNull String addressId,
        @NotBlank String paymentMethod
) {}
