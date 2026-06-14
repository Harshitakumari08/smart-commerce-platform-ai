package com.smartcommerce.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartcommerce.dto.request.CategoryRequest;
import com.smartcommerce.dto.request.ProductRequest;
import com.smartcommerce.entity.Category;
import com.smartcommerce.entity.Product;
import com.smartcommerce.repository.CategoryRepository;
import com.smartcommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductCatalogTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    private Category electronicsCategory;
    private Product iphoneProduct;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        electronicsCategory = Category.builder()
                .name("Electronics")
                .slug("electronics")
                .description("Electronic gadgets")
                .imageUrl("electronics-url")
                .isActive(true)
                .build();
        electronicsCategory = categoryRepository.save(electronicsCategory);

        iphoneProduct = Product.builder()
                .category(electronicsCategory)
                .name("iPhone 16")
                .slug("iphone-16")
                .shortDescription("Latest Apple phone")
                .description("Detailed specs of iPhone 16")
                .sku("IPH16-128")
                .price(new BigDecimal("999.99"))
                .comparePrice(new BigDecimal("1099.99"))
                .stockQuantity(50)
                .brand("Apple")
                .imageUrl("iphone-url")
                .thumbnailUrl("iphone-thumb-url")
                .rating(4.8)
                .reviewCount(10)
                .isActive(true)
                .isFeatured(true)
                .build();
        iphoneProduct = productRepository.save(iphoneProduct);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCategoryCRUD_AdminSuccess() throws Exception {
        // 1. Create Category
        CategoryRequest newCategory = new CategoryRequest("Books", "books", "Read books", "books-url", true);
        mockMvc.perform(post("/api/v1/admin/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCategory)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Books"))
                .andExpect(jsonPath("$.slug").value("books"));

        // 2. Update Category
        Category category = categoryRepository.findBySlug("books").orElseThrow();
        CategoryRequest updateCategory = new CategoryRequest("Rare Books", "rare-books", "Expensive books", "books-url", true);
        mockMvc.perform(put("/api/v1/admin/categories/" + category.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCategory)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Rare Books"))
                .andExpect(jsonPath("$.slug").value("rare-books"));

        // 3. Delete Category
        mockMvc.perform(delete("/api/v1/admin/categories/" + category.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        Category deleted = categoryRepository.findById(category.getId()).orElseThrow();
        assertFalse(deleted.isActive());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testProductCRUD_AdminSuccess() throws Exception {
        // 1. Create Product
        ProductRequest newProduct = new ProductRequest(
                electronicsCategory.getId(),
                "MacBook Air M3",
                "",
                "Latest slim laptop",
                "MACAIR-M3",
                new BigDecimal("1299.99"),
                new BigDecimal("1399.99"),
                25,
                "Apple",
                "macbook-url",
                "macbook-thumb",
                true,
                false
        );

        mockMvc.perform(post("/api/v1/admin/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("MacBook Air M3"))
                .andExpect(jsonPath("$.sku").value("MACAIR-M3"));

        // 2. Update Product
        Product product = productRepository.findBySlug("macbook-air-m3").orElseThrow();
        ProductRequest updateProduct = new ProductRequest(
                electronicsCategory.getId(),
                "MacBook Air M3 Core",
                "",
                "Updated slim laptop",
                "MACAIR-M3",
                new BigDecimal("1249.99"),
                new BigDecimal("1399.99"),
                20,
                "Apple",
                "macbook-url",
                "macbook-thumb",
                true,
                false
        );

        mockMvc.perform(put("/api/v1/admin/products/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("MacBook Air M3 Core"))
                .andExpect(jsonPath("$.price").value(1249.99));

        // 3. Delete Product
        mockMvc.perform(delete("/api/v1/admin/products/" + product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        Product deleted = productRepository.findById(product.getId()).orElseThrow();
        assertFalse(deleted.isActive());
    }

    @Test
    void testGetProducts_PublicListAndFiltersings() throws Exception {
        // Create another category and product
        Category clothingCategory = Category.builder()
                .name("Clothing")
                .slug("clothing")
                .isActive(true)
                .build();
        clothingCategory = categoryRepository.save(clothingCategory);

        Product tshirtProduct = Product.builder()
                .category(clothingCategory)
                .name("Summer T-Shirt")
                .slug("summer-t-shirt")
                .sku("TSHIRT-S")
                .price(new BigDecimal("19.99"))
                .stockQuantity(100)
                .brand("Nike")
                .isActive(true)
                .build();
        productRepository.save(tshirtProduct);

        // 1. Get Categories List
        mockMvc.perform(get("/api/v1/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        // 2. Get Products List
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));

        // 3. Filter by Category
        mockMvc.perform(get("/api/v1/products").param("category", "electronics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("iPhone 16"));

        // 4. Search Products
        mockMvc.perform(get("/api/v1/products").param("search", "shirt"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Summer T-Shirt"));

        // 5. Price filter
        mockMvc.perform(get("/api/v1/products").param("minPrice", "100").param("maxPrice", "2000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("iPhone 16"));

        // 6. Sorting (Price high to low)
        mockMvc.perform(get("/api/v1/products").param("sort", "price_desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("iPhone 16"));

        // 7. Get Detail by Slug
        mockMvc.perform(get("/api/v1/products/iphone-16"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("iPhone 16"))
                .andExpect(jsonPath("$.brand").value("Apple"));
    }

    @Test
    void testGetProducts_UnauthorizedPost() throws Exception {
        ProductRequest newProduct = new ProductRequest(
                electronicsCategory.getId(),
                "iPhone 16 Pro",
                "",
                "Latest Apple Pro phone",
                "IPH16PRO",
                new BigDecimal("1099.99"),
                null,
                10,
                "Apple",
                "url",
                "url",
                true,
                true
        );

        mockMvc.perform(post("/api/v1/admin/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isForbidden());
    }
}
