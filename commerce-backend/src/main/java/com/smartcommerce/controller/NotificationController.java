package com.smartcommerce.controller;

import com.smartcommerce.dto.response.ApiSuccessResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@Tag(name = "Notifications", description = "Notification endpoints")
public class NotificationController {

    @GetMapping
    public ApiSuccessResponse<String> list() { return ApiSuccessResponse.of("Notifications"); }

    @PutMapping("/{id}/read")
    public ApiSuccessResponse<String> markRead(@PathVariable String id) { return ApiSuccessResponse.of("Notification marked read"); }

    @PutMapping("/read-all")
    public ApiSuccessResponse<String> markAllRead() { return ApiSuccessResponse.of("All notifications marked read"); }
}
