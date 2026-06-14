package com.smartcommerce.service;

import com.smartcommerce.dto.request.ProductRequest;
import com.smartcommerce.dto.response.ProductResponse;
import org.springframework.data.domain.Page;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);
    ProductResponse updateProduct(String id, ProductRequest request);
    void deleteProduct(String id);
    ProductResponse getProduct(String slug);
    Page<ProductResponse> getProducts(String search, String category, Double minPrice, Double maxPrice, String sort, int page, int size);
}
