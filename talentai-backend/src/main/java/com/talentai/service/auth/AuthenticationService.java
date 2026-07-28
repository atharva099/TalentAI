package com.talentai.service.auth;

import com.talentai.dto.request.AuthRequest;
import com.talentai.dto.request.UserRequest;
import com.talentai.dto.response.AuthResponse;

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
}
