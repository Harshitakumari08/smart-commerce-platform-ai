package com.smartcommerce.controller;

import com.smartcommerce.dto.response.ApiSuccessResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inventory")
@Tag(name = "Inventory", description = "Inventory and stock endpoints")
public class InventoryController {

    @GetMapping
    public ApiSuccessResponse<String> list() { return ApiSuccessResponse.of("Inventory"); }

    @GetMapping("/{productId}")
    public ApiSuccessResponse<String> getByProduct(@PathVariable String productId) { return ApiSuccessResponse.of("Inventory for product " + productId); }

    @PutMapping("/{productId}")
    public ApiSuccessResponse<String> update(@PathVariable String productId) { return ApiSuccessResponse.of("Inventory updated"); }
}
