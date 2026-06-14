package com.smartcommerce.controller;

import com.smartcommerce.dto.request.OrderRequest;
import com.smartcommerce.dto.response.ApiSuccessResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Orders", description = "Order lifecycle endpoints")
public class OrderController {

    @PostMapping
    public ApiSuccessResponse<String> create(@Valid @RequestBody OrderRequest request) { return ApiSuccessResponse.of("Order created"); }

    @GetMapping
    public ApiSuccessResponse<String> list() { return ApiSuccessResponse.of("Orders"); }

    @GetMapping("/{id}")
    public ApiSuccessResponse<String> getById(@PathVariable String id) { return ApiSuccessResponse.of("Order " + id); }

    @PutMapping("/{id}/cancel")
    public ApiSuccessResponse<String> cancel(@PathVariable String id) { return ApiSuccessResponse.of("Order cancelled"); }
}
