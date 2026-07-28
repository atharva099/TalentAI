package com.talentai.service.auth;

import java.util.Locale;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.talentai.dto.request.AuthRequest;
import com.talentai.dto.request.UserRequest;
import com.talentai.dto.response.AuthResponse;
import com.talentai.dto.response.UserResponse;
import com.talentai.exception.ApplicationException;
import com.talentai.exception.ErrorCode;
import com.talentai.security.JwtService;
import com.talentai.service.user.UserService;

import lombok.RequiredArgsConstructor;

/**
 * Implements registration and login workflows using the User module and Spring Security.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    /**
     * {@inheritDoc}
     */
    @Override
    public AuthResponse register(UserRequest request) {
        UserResponse user = userService.createUser(request);
        return createAuthResponse(user.getEmail());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AuthResponse login(AuthRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase(Locale.ROOT);

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(normalizedEmail, request.getPassword()));
            return createAuthResponse(authentication.getName());
        } catch (AuthenticationException exception) {
            throw new ApplicationException(ErrorCode.AUTH_INVALID_CREDENTIALS);
        }
    }

    private AuthResponse createAuthResponse(String email) {
        return AuthResponse.builder()
                .accessToken(jwtService.generateAccessToken(email))
                .tokenType("Bearer")
                .expiresInSeconds(jwtService.getAccessTokenExpirationSeconds())
                .build();
    }
}
