package com.smartcommerce.controller;

import com.smartcommerce.dto.request.ReviewRequest;
import com.smartcommerce.dto.response.ApiSuccessResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Reviews", description = "Review lifecycle endpoints")
public class ReviewController {

    @GetMapping("/api/v1/products/{id}/reviews")
    public ApiSuccessResponse<String> list(@PathVariable String id) { return ApiSuccessResponse.of("Reviews for product " + id); }

    @PostMapping("/api/v1/products/{id}/reviews")
    public ApiSuccessResponse<String> create(@PathVariable String id, @Valid @RequestBody ReviewRequest request) { return ApiSuccessResponse.of("Review created"); }

    @PutMapping("/api/v1/reviews/{id}")
    public ApiSuccessResponse<String> update(@PathVariable String id, @Valid @RequestBody ReviewRequest request) { return ApiSuccessResponse.of("Review updated"); }

    @DeleteMapping("/api/v1/reviews/{id}")
    public ApiSuccessResponse<String> delete(@PathVariable String id) { return ApiSuccessResponse.of("Review deleted"); }
}
