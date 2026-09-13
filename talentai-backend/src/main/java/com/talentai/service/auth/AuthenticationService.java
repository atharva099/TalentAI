package com.talentai.service.auth;

import com.talentai.dto.request.AuthRequest;
import com.talentai.dto.request.UserRequest;
import com.talentai.dto.response.AuthResponse;
import com.talentai.dto.response.CurrentUserResponse;

/**
 * Defines registration and credential-authentication operations.
 */
public interface AuthenticationService {

    /**
     * Registers a user and issues an access token.
     *
     * @param request validated user registration request
     * @return authentication result
     */
    AuthResponse register(UserRequest request);

    /**
     * Authenticates user credentials and issues an access token.
     *
     * @param request validated login request
     * @return authentication result
     */
    AuthResponse login(AuthRequest request);

    /**
     * Retrieves the currently authenticated user's safe profile.
     *
     * @param email authenticated user's email address
     * @return current user response
     */
    CurrentUserResponse getCurrentUser(String email);
}
