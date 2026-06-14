package com.smartcommerce.service;

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
import com.smartcommerce.service.impl.AuthServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthServiceImpl authService;

    private RoleEntity customerRole;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        customerRole = RoleEntity.builder()
                .id("role-uuid")
                .roleName("CUSTOMER")
                .description("Customer role")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        userEntity = UserEntity.builder()
                .id("user-uuid")
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .phone("+919876543210")
                .passwordHash("hashed-password")
                .role(customerRole)
                .build();
    }

    @Test
    void register_NewUser_Success() {
        AuthRegisterRequest request = new AuthRegisterRequest(
                "John", "Doe", "john@example.com", "+919876543210", "StrongPassword123"
        );

        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(roleRepository.findByRoleName("CUSTOMER")).thenReturn(Optional.of(customerRole));
        when(passwordEncoder.encode("StrongPassword123")).thenReturn("hashed-password");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        assertDoesNotThrow(() -> authService.register(request));

        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void register_DuplicateEmail_ConflictException() {
        AuthRegisterRequest request = new AuthRegisterRequest(
                "John", "Doe", "john@example.com", "+919876543210", "StrongPassword123"
        );

        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        assertThrows(ConflictException.class, () -> authService.register(request));
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void login_ValidCredentials_Success() {
        AuthLoginRequest request = new AuthLoginRequest("john@example.com", "StrongPassword123");

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches("StrongPassword123", "hashed-password")).thenReturn(true);
        when(jwtTokenProvider.createAccessToken(eq("user-uuid"), any())).thenReturn("access-token");
        when(jwtTokenProvider.createRefreshToken("user-uuid")).thenReturn("refresh-token");

        AuthLoginResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("access-token", response.accessToken());
        assertEquals("refresh-token", response.refreshToken());
        assertEquals(3600L, response.expiresIn());
    }

    @Test
    void login_WrongPassword_BadRequestException() {
        AuthLoginRequest request = new AuthLoginRequest("john@example.com", "WrongPassword123");

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches("WrongPassword123", "hashed-password")).thenReturn(false);

        assertThrows(BadRequestException.class, () -> authService.login(request));
    }

    @Test
    void login_InvalidEmail_BadRequestException() {
        AuthLoginRequest request = new AuthLoginRequest("notfound@example.com", "Password123");

        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> authService.login(request));
    }

    @Test
    void refreshToken_ValidToken_Success() {
        RefreshTokenRequest request = new RefreshTokenRequest("valid-refresh-token");
        
        Claims claims = new DefaultClaims(Map.of("sub", "user-uuid"));
        @SuppressWarnings("unchecked")
        Jws<Claims> jwsClaims = mock(Jws.class);
        when(jwsClaims.getPayload()).thenReturn(claims);
        
        when(jwtTokenProvider.parse("valid-refresh-token")).thenReturn(jwsClaims);
        when(userRepository.findById("user-uuid")).thenReturn(Optional.of(userEntity));
        when(jwtTokenProvider.createAccessToken(eq("user-uuid"), any())).thenReturn("new-access-token");

        AuthLoginResponse response = authService.refreshToken(request);

        assertNotNull(response);
        assertEquals("new-access-token", response.accessToken());
        assertEquals("valid-refresh-token", response.refreshToken());
    }

    @Test
    void refreshToken_InvalidToken_UnauthorizedException() {
        RefreshTokenRequest request = new RefreshTokenRequest("invalid-refresh-token");

        when(jwtTokenProvider.parse("invalid-refresh-token")).thenThrow(new RuntimeException("Expired token"));

        assertThrows(UnauthorizedException.class, () -> authService.refreshToken(request));
    }

    @Test
    void getCurrentUser_ValidId_Success() {
        when(userRepository.findById("user-uuid")).thenReturn(Optional.of(userEntity));

        CurrentUserResponse response = authService.getCurrentUser("user-uuid");

        assertNotNull(response);
        assertEquals("user-uuid", response.id());
        assertEquals("John", response.firstName());
        assertEquals("john@example.com", response.email());
        assertEquals("CUSTOMER", response.role());
    }
}
