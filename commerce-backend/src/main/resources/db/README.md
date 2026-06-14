# Smart Commerce Database Design

## Migration Order
1. roles
2. users
3. refresh_tokens
4. addresses
5. categories
6. products
7. product_images
8. inventory
9. carts
10. cart_items
11. wishlists
12. wishlist_items
13. orders
14. order_items
15. payments
16. reviews
17. notifications
18. product_views
19. search_history
20. recommendation_scores
21. coupons
22. coupon_usage
23. analytics_events

## ER Relationships
- roles 1 -> many users
- users 1 -> many addresses
- users 1 -> 1 carts
- users 1 -> 1 wishlists
- users 1 -> many orders
- categories 1 -> many products
- products 1 -> 1 inventory
- products 1 -> many reviews
- products 1 -> many product_images
- orders 1 -> many order_items
- orders 1 -> 1 payments

## Optimization Notes
- Add partial indexes for active products and recent analytics events in production.
- Use BRIN indexes on large event tables if event volume is high.
- Keep JSONB payloads small and queryable.
- Enable connection pooling with PgBouncer for scale.
- Partition analytics_events by event_timestamp if volume exceeds millions of rows.

## Recommended Redis Cache Keys
- product:catalog:{categorySlug}
- product:detail:{productId}
- user:cart:{userId}
- user:wishlist:{userId}
- recommendation:top:{userId}
- analytics:summary:{date}
- search:popular:{queryHash}
