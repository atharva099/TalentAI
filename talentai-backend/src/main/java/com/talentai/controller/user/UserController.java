package com.talentai.controller.user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;

import com.talentai.dto.request.UserUpdateRequest;
import com.talentai.dto.response.ApiResponse;
import com.talentai.dto.response.UserResponse;
import com.talentai.service.user.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

/**
 * Exposes authenticated CRUD endpoints for user profiles.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private static final String REQUEST_ID_HEADER = "X-Request-Id";

    private final UserService userService;

    /**
     * Retrieves all users.
     *
     * @param request current HTTP request
     * @return user collection response
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers(HttpServletRequest request) {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully.", users, resolveRequestId(request)));
    }

    /**
     * Retrieves one user by identifier.
     *
     * @param userId internal user identifier
     * @param request current HTTP request
     * @return user response
     */
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(
            @PathVariable @Positive(message = "User ID must be positive.") Long userId,
            HttpServletRequest request) {
        UserResponse user = userService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully.", user, resolveRequestId(request)));
    }

    /**
     * Updates user profile information.
     *
     * @param userId internal user identifier
     * @param updateRequest validated user profile update payload
     * @param request current HTTP request
     * @return updated user response
     */
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable @Positive(message = "User ID must be positive.") Long userId,
            @Valid @RequestBody UserUpdateRequest updateRequest,
            HttpServletRequest request) {
        UserResponse user = userService.updateUser(userId, updateRequest);
        return ResponseEntity.ok(ApiResponse.success("User updated successfully.", user, resolveRequestId(request)));
    }

    /**
     * Deletes a user.
     *
     * @param userId internal user identifier
     * @param request current HTTP request
     * @return successful deletion response
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable @Positive(message = "User ID must be positive.") Long userId,
            HttpServletRequest request) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully.", null, resolveRequestId(request)));
    }

    private String resolveRequestId(HttpServletRequest request) {
        return request.getHeader(REQUEST_ID_HEADER);
    }
}
