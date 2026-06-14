package com.smartcommerce.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AuthRegisterRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @Email @NotBlank String email,
        @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Invalid phone number") String phone,
        @NotBlank @Size(min = 8, max = 72) String password
) {}
