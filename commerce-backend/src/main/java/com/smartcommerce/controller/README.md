# Backend API Architecture

This scaffold includes:
- Request DTOs under dto/request
- Response DTOs under dto/response
- Controllers for auth, categories, products, orders, reviews, recommendations, analytics, notifications, coupons, inventory, and admin flows
- Service interfaces and implementations under service and service/impl
- Repository interfaces under repository
- Validation helpers under validation

## Role Permissions Matrix
| Module | CUSTOMER | ADMIN | SUPER_ADMIN |
|---|---|---|---|
| Auth login/register | Yes | Yes | Yes |
| Product catalog read | Yes | Yes | Yes |
| Cart / wishlist / orders | Yes | No | Yes |
| Admin management | No | Yes | Yes |
| Analytics dashboard | No | Yes | Yes |
