package com.talentai.dto.response;

import lombok.Builder;
import lombok.Value;

/**
 * Authentication result containing a short-lived JWT access token.
 */
@Value
@Builder
public class AuthResponse {

    String accessToken;
    String tokenType;
    long expiresInSeconds;
}
