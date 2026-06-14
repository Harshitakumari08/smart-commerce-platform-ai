package com.smartcommerce.service.impl;

import com.smartcommerce.dto.request.AuthLoginRequest;
import com.smartcommerce.dto.request.AuthRegisterRequest;
import com.smartcommerce.dto.request.RefreshTokenRequest;
import com.smartcommerce.dto.response.AuthLoginResponse;
import com.smartcommerce.dto.response.CurrentUserResponse;
import com.smartcommerce.entity.RoleEntity;
import com.smartcommerce.entity.UserEntity;
import com.smartcommerce.exception.BadRequestException;
import com.smartcommerce.exception.ConflictException;
import com.smartcommerce.exception.UnauthorizedException;
import com.smartcommerce.repository.RoleRepository;
import com.smartcommerce.repository.UserRepository;
import com.smartcommerce.security.JwtTokenProvider;
import com.smartcommerce.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void register(AuthRegisterRequest request) {
        if (userRepository.existsByEmail(request.email().trim().toLowerCase())) {
            throw new ConflictException("Email already registered");
        }
        RoleEntity role = roleRepository.findByRoleName("CUSTOMER")
                .orElseGet(() -> roleRepository.save(RoleEntity.builder().roleName("CUSTOMER").description("Customer role").createdAt(Instant.now()).updatedAt(Instant.now()).build()));

        UserEntity user = UserEntity.builder()
                .firstName(request.firstName().trim())
                .lastName(request.lastName().trim())
                .email(request.email().trim().toLowerCase())
                .phone(request.phone())
                .passwordHash(passwordEncoder.encode(request.password()))
                .role(role)
                .build();
        userRepository.save(user);
    }

    @Override
    public AuthLoginResponse login(AuthLoginRequest request) {
        UserEntity user = userRepository.findByEmail(request.email().trim().toLowerCase())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BadRequestException("Invalid credentials");
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), Map.of("userId", user.getId(), "email", user.getEmail(), "role", user.getRole().getRoleName()));
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());
        return new AuthLoginResponse(accessToken, refreshToken, 3600L);
    }

    @Override
    public AuthLoginResponse refreshToken(RefreshTokenRequest request) {
        try {
            var claims = jwtTokenProvider.parse(request.refreshToken()).getPayload();
            String userId = claims.getSubject();
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));
            String accessToken = jwtTokenProvider.createAccessToken(user.getId(), Map.of("userId", user.getId(), "email", user.getEmail(), "role", user.getRole().getRoleName()));
            return new AuthLoginResponse(accessToken, request.refreshToken(), 3600L);
        } catch (Exception ex) {
            throw new UnauthorizedException("Invalid refresh token");
        }
    }

    @Override
    public CurrentUserResponse getCurrentUser(String userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("Invalid token"));
        return new CurrentUserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole().getRoleName());
    }
}
