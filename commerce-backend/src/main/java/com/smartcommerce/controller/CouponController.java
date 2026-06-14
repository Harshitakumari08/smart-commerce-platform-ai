package com.smartcommerce.controller;

import com.smartcommerce.dto.response.ApiSuccessResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/coupons")
@Tag(name = "Coupons", description = "Coupon management endpoints")
public class CouponController {

    @GetMapping
    public ApiSuccessResponse<String> list() { return ApiSuccessResponse.of("Coupons"); }

    @PostMapping
    public ApiSuccessResponse<String> create() { return ApiSuccessResponse.of("Coupon created"); }

    @PostMapping("/apply")
    public ApiSuccessResponse<String> apply() { return ApiSuccessResponse.of("Coupon applied"); }
}
