package com.smartcommerce.controller;

import com.smartcommerce.dto.request.ProductRequest;
import com.smartcommerce.dto.response.ApiSuccessResponse;
import com.smartcommerce.dto.response.ProductResponse;
import com.smartcommerce.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Products", description = "Product catalog endpoints")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/api/v1/products")
    public Page<ProductResponse> getProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return productService.getProducts(search, category, minPrice, maxPrice, sort, page, size);
    }

    @GetMapping("/api/v1/products/{slug}")
    public ProductResponse getProduct(@PathVariable String slug) {
        return productService.getProduct(slug);
    }

    @PostMapping("/api/v1/admin/products")
    public ProductResponse create(@Valid @RequestBody ProductRequest request) {
        return productService.createProduct(request);
    }

    @PutMapping("/api/v1/admin/products/{id}")
    public ProductResponse update(@PathVariable String id, @Valid @RequestBody ProductRequest request) {
        return productService.updateProduct(id, request);
    }

    @DeleteMapping("/api/v1/admin/products/{id}")
    public ApiSuccessResponse<Void> delete(@PathVariable String id) {
        productService.deleteProduct(id);
        return ApiSuccessResponse.success("Product deleted successfully");
    }
}
