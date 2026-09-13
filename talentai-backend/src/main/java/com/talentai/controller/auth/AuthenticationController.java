package com.talentai.controller.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.talentai.dto.request.AuthRequest;
import com.talentai.dto.request.UserRequest;
import com.talentai.dto.response.ApiResponse;
import com.talentai.dto.response.AuthResponse;
import com.talentai.dto.response.CurrentUserResponse;
import com.talentai.service.auth.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import lombok.RequiredArgsConstructor;

/**
 * Exposes public registration and login endpoints.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private static final String REQUEST_ID_HEADER = "X-Request-Id";

    private final AuthenticationService authenticationService;

    /**
     * Registers a user and returns a JWT access token.
     *
     * @param request validated registration payload
     * @param httpRequest current HTTP request
     * @return created authentication response
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody UserRequest request,
            HttpServletRequest httpRequest) {
        AuthResponse response = authenticationService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully.", response, resolveRequestId(httpRequest)));
    }

    /**
     * Authenticates a user and returns a JWT access token.
     *
     * @param request validated login payload
     * @param httpRequest current HTTP request
     * @return successful authentication response
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody AuthRequest request,
            HttpServletRequest httpRequest) {
        AuthResponse response = authenticationService.login(request);

        return ResponseEntity.ok(
                ApiResponse.success("User authenticated successfully.", response, resolveRequestId(httpRequest)));
    }

    /**
     * Retrieves the profile and persisted roles for the authenticated user.
     *
     * @param authentication current JWT-authenticated principal
     * @param httpRequest current HTTP request
     * @return safe current user response
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<CurrentUserResponse>> getCurrentUser(
            Authentication authentication,
            HttpServletRequest httpRequest) {
        CurrentUserResponse response = authenticationService.getCurrentUser(authentication.getName());

        return ResponseEntity.ok(
                ApiResponse.success("Current user retrieved successfully.", response, resolveRequestId(httpRequest)));
    }

    private String resolveRequestId(HttpServletRequest request) {
        return request.getHeader(REQUEST_ID_HEADER);
    }
}
