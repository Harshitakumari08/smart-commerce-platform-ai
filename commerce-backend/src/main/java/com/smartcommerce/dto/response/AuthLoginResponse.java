package com.smartcommerce.dto.response;

public record AuthLoginResponse(String accessToken, String refreshToken, long expiresIn) {}
