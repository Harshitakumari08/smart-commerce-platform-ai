package com.smartcommerce.controller;

import com.smartcommerce.dto.response.ApiSuccessResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@Tag(name = "Admin", description = "Administrative management endpoints")
public class AdminController {

    @GetMapping("/users")
    public ApiSuccessResponse<String> listUsers() { return ApiSuccessResponse.of("Admin users"); }

    @PutMapping("/users/{id}/block")
    public ApiSuccessResponse<String> blockUser(@PathVariable String id) { return ApiSuccessResponse.of("User blocked"); }
}
