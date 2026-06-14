package com.smartcommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartcommerce.dto.request.AuthLoginRequest;
import com.smartcommerce.dto.request.AuthRegisterRequest;
import com.smartcommerce.dto.response.AuthLoginResponse;
import com.smartcommerce.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    void register_ValidRequest_Success() throws Exception {
        AuthRegisterRequest request = new AuthRegisterRequest(
                "John", "Doe", "john.doe@example.com", "+919876543210", "Password123"
        );

        doNothing().when(authService).register(any(AuthRegisterRequest.class));

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    void register_InvalidEmail_BadRequest() throws Exception {
        AuthRegisterRequest request = new AuthRegisterRequest(
                "John", "Doe", "invalid-email", "+919876543210", "Password123"
        );

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_ValidRequest_ReturnsToken() throws Exception {
        AuthLoginRequest request = new AuthLoginRequest("john@example.com", "Password123");
        AuthLoginResponse response = new AuthLoginResponse("jwt-access-token", "jwt-refresh-token", 3600L);

        when(authService.login(any(AuthLoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("jwt-access-token"))
                .andExpect(jsonPath("$.refreshToken").value("jwt-refresh-token"))
                .andExpect(jsonPath("$.expiresIn").value(3600));
    }

    @Test
    void getMe_Unauthorized_WithoutToken() throws Exception {
        mockMvc.perform(get("/api/v1/auth/me"))
                .andExpect(status().isUnauthorized());
    }
}
