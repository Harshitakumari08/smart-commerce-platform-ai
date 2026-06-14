package com.smartcommerce.controller;

import com.smartcommerce.dto.response.ApiSuccessResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/recommendations")
@Tag(name = "Recommendations", description = "Recommendation engine endpoints")
public class RecommendationController {

    @GetMapping
    public ApiSuccessResponse<String> list() { return ApiSuccessResponse.of("Recommendations"); }

    @GetMapping("/trending")
    public ApiSuccessResponse<String> trending() { return ApiSuccessResponse.of("Trending recommendations"); }

    @GetMapping("/category/{categoryId}")
    public ApiSuccessResponse<String> byCategory(@PathVariable String categoryId) { return ApiSuccessResponse.of("Category recommendations"); }

    @GetMapping("/similar/{productId}")
    public ApiSuccessResponse<String> similar(@PathVariable String productId) { return ApiSuccessResponse.of("Similar products"); }
}
