package com.smartcommerce.validation;

public final class ValidationMessages {
    public static final String EMAIL_REQUIRED = "Email is required";
    public static final String PASSWORD_INVALID = "Password must be at least 8 characters";
    public static final String PHONE_INVALID = "Phone number is invalid";
    public static final String PRICE_INVALID = "Price must be non-negative";
    public static final String STOCK_INVALID = "Stock quantity cannot be negative";
    public static final String RATING_INVALID = "Rating must be between 1 and 5";
    public static final String COUPON_INVALID = "Coupon code is invalid";

    private ValidationMessages() {}
}
