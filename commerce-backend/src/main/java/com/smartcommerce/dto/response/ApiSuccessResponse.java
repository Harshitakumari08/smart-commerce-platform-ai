package com.smartcommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiSuccessResponse<T>(boolean success, String message, T data) {
    public static <T> ApiSuccessResponse<T> of(T data) {
        return new ApiSuccessResponse<>(true, "Operation successful", data);
    }

    public static ApiSuccessResponse<Void> success(String message) {
        return new ApiSuccessResponse<>(true, message, null);
    }
}
