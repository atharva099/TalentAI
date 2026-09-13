package com.talentai.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private CustomUserDetailsService userDetailsService;

    private JwtAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        filter = new JwtAuthenticationFilter(jwtService, userDetailsService);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void excludesLoginEndpoint() {
        assertThat(filter.shouldNotFilter(requestFor("/api/v1/auth/login"))).isTrue();
    }

    @Test
    void excludesRegisterEndpoint() {
        assertThat(filter.shouldNotFilter(requestFor("/api/v1/auth/register"))).isTrue();
    }

    @Test
    void doesNotExcludeCurrentUserEndpoint() {
        assertThat(filter.shouldNotFilter(requestFor("/api/v1/auth/me"))).isFalse();
    }

    @Test
    void authenticatesRequestWithValidBearerToken() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        var userDetails = org.springframework.security.core.userdetails.User
                .withUsername("user@example.com")
                .password("")
                .authorities("ROLE_CANDIDATE")
                .build();

        when(request.getHeader("Authorization")).thenReturn("Bearer opaque-test-value");
        when(jwtService.extractUsername("opaque-test-value")).thenReturn("user@example.com");
        when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(userDetails);
        when(jwtService.isTokenValid("opaque-test-value", "user@example.com")).thenReturn(true);

        filter.doFilterInternal(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();
        assertThat(authentication.getName()).isEqualTo("user@example.com");
        assertThat(authentication.getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_CANDIDATE");
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void clearsContextWhenBearerTokenIsInvalid() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer opaque-test-value");
        when(jwtService.extractUsername("opaque-test-value")).thenThrow(new JwtException("invalid token"));

        filter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }

    private HttpServletRequest requestFor(String servletPath) {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getServletPath()).thenReturn(servletPath);
        return request;
    }
}
