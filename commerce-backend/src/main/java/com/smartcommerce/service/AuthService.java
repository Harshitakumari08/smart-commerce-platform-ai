package com.smartcommerce.service;

import com.smartcommerce.dto.request.AuthLoginRequest;
import com.smartcommerce.dto.request.AuthRegisterRequest;
import com.smartcommerce.dto.request.RefreshTokenRequest;
import com.smartcommerce.dto.response.AuthLoginResponse;
import com.smartcommerce.dto.response.CurrentUserResponse;

public interface AuthService {
    void register(AuthRegisterRequest request);
    AuthLoginResponse login(AuthLoginRequest request);
    AuthLoginResponse refreshToken(RefreshTokenRequest request);
    CurrentUserResponse getCurrentUser(String userId);
}
