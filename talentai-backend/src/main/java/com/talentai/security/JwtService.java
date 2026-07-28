package com.talentai.security;

import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Generates and validates signed JWT access tokens for TalentAI users.
 */
@Service
public class JwtService {

    private final SecretKey signingKey;
    private final long accessTokenExpirationSeconds;

    /**
     * Creates the JWT service from externalized application configuration.
     *
     * @param jwtSecret Base64-encoded HMAC signing key
     * @param accessTokenExpirationSeconds token lifetime in seconds
     */
    public JwtService(
            @Value("${security.jwt.secret}") String jwtSecret,
            @Value("${security.jwt.access-token-expiration-seconds}") long accessTokenExpirationSeconds) {
        if (accessTokenExpirationSeconds <= 0) {
            throw new IllegalArgumentException("JWT access token expiration must be positive.");
        }

        this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
        this.accessTokenExpirationSeconds = accessTokenExpirationSeconds;
    }

    /**
     * Generates a signed access token for a user email address.
     *
     * @param email authenticated user email address
     * @return compact JWT access token
     */
    public String generateAccessToken(String email) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusSeconds(accessTokenExpirationSeconds);

        return Jwts.builder()
                .subject(email)
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiresAt))
                .signWith(signingKey)
                .compact();
    }

    /**
     * Extracts the authenticated user email from a signed token.
     *
     * @param token compact JWT access token
     * @return token subject email
     */
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    /**
     * Verifies token signature, expiry, and subject ownership.
     *
     * @param token compact JWT access token
     * @param email expected user email address
     * @return true when the token is valid for the supplied user
     */
    public boolean isTokenValid(String token, String email) {
        try {
            return email.equals(extractUsername(token));
        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }

    /**
     * Returns the configured access-token lifetime.
     *
     * @return token lifetime in seconds
     */
    public long getAccessTokenExpirationSeconds() {
        return accessTokenExpirationSeconds;
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
