package com.smartcommerce.repository;

import com.smartcommerce.entity.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecifications {
    
    public static Specification<Product> hasSearch(String search) {
        return (root, query, cb) -> {
            if (search == null || search.trim().isEmpty()) {
                return cb.conjunction();
            }
            String pattern = "%" + search.trim().toLowerCase() + "%";
            return cb.or(
                cb.like(cb.lower(root.get("name")), pattern),
                cb.like(cb.lower(root.get("brand")), pattern),
                cb.like(cb.lower(root.get("sku")), pattern)
            );
        };
    }

    public static Specification<Product> hasCategory(String categorySlug) {
        return (root, query, cb) -> {
            if (categorySlug == null || categorySlug.trim().isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(cb.lower(root.get("category").get("slug")), categorySlug.trim().toLowerCase());
        };
    }

    public static Specification<Product> hasPriceRange(Double minPrice, Double maxPrice) {
        return (root, query, cb) -> {
            if (minPrice == null && maxPrice == null) {
                return cb.conjunction();
            }
            if (minPrice != null && maxPrice != null) {
                return cb.between(root.get("price"), minPrice, maxPrice);
            } else if (minPrice != null) {
                return cb.ge(root.get("price"), minPrice);
            } else {
                return cb.le(root.get("price"), maxPrice);
            }
        };
    }

    public static Specification<Product> isActive() {
        return (root, query, cb) -> cb.equal(root.get("isActive"), true);
    }
}
