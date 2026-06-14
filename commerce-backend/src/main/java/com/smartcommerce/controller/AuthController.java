package com.smartcommerce.controller;

import com.smartcommerce.dto.request.AuthLoginRequest;
import com.smartcommerce.dto.request.AuthRegisterRequest;
import com.smartcommerce.dto.request.RefreshTokenRequest;
import com.smartcommerce.dto.response.ApiSuccessResponse;
import com.smartcommerce.dto.response.AuthLoginResponse;
import com.smartcommerce.dto.response.CurrentUserResponse;
import com.smartcommerce.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication and account lifecycle endpoints")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new account", description = "Creates a customer account with BCrypt password hashing")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Registered successfully")})
    public ApiSuccessResponse<Void> register(@Valid @RequestBody AuthRegisterRequest request) {
        authService.register(request);
        return ApiSuccessResponse.success("User registered successfully");
    }

    @PostMapping("/login")
    public AuthLoginResponse login(@Valid @RequestBody AuthLoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/logout")
    public ApiSuccessResponse<String> logout() {
        return ApiSuccessResponse.of("Logged out");
    }

    @PostMapping("/refresh-token")
    public AuthLoginResponse refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return authService.refreshToken(request);
    }

    @GetMapping("/me")
    public CurrentUserResponse me() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return authService.getCurrentUser(auth.getName());
    }
}
