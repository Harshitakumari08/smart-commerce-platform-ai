package com.smartcommerce.controller;

import com.smartcommerce.dto.response.ApiSuccessResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/analytics")
@Tag(name = "Analytics", description = "Analytics dashboard endpoints")
public class AnalyticsController {

    @GetMapping("/dashboard")
    public ApiSuccessResponse<String> dashboard() { return ApiSuccessResponse.of("Dashboard summary"); }

    @GetMapping("/revenue")
    public ApiSuccessResponse<String> revenue() { return ApiSuccessResponse.of("Revenue analytics"); }
}
