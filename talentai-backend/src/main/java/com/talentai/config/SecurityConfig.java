package com.talentai.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.talentai.security.CustomUserDetailsService;
import com.talentai.security.JwtAuthenticationFilter;

/**
 * Defines the initial stateless Spring Security baseline for TalentAI.
 *
 * <p>Authentication endpoints are public. All other requests require a valid
 * JWT access token.</p>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures the stateless JWT authentication filter chain.
     *
     * @param http Spring Security HTTP configuration
     * @return configured security filter chain
     * @throws Exception if the filter chain cannot be built
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            DaoAuthenticationProvider authenticationProvider,
            JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/users").hasAnyRole("RECRUITER", "PLATFORM_ADMIN")
                        .requestMatchers("/api/v1/jobs/**").hasAnyRole("RECRUITER", "PLATFORM_ADMIN")
                        .requestMatchers("/api/v1/candidates/**").hasAnyRole("RECRUITER", "PLATFORM_ADMIN")
                        .requestMatchers("/api/v1/resumes/**").hasAnyRole("RECRUITER", "PLATFORM_ADMIN")
                        .requestMatchers("/api/v1/dashboard/**").hasAnyRole("RECRUITER", "PLATFORM_ADMIN")
                        .requestMatchers("/api/v1/auth/me").authenticated()
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Allows the local Vite frontend to call the stateless API.
     *
     * @return centralized CORS configuration for API requests
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Content-Type", "Authorization", "X-Request-Id"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Configures credential authentication against persisted user credentials.
     *
     * @param userDetailsService user lookup service
     * @param passwordEncoder BCrypt password encoder
     * @return DAO authentication provider
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            CustomUserDetailsService userDetailsService,
            BCryptPasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    /**
     * Provides the authentication manager used by the login workflow.
     *
     * @param authenticationProvider configured credential provider
     * @return authentication manager
     */
    @Bean
    public AuthenticationManager authenticationManager(DaoAuthenticationProvider authenticationProvider) {
        return new ProviderManager(authenticationProvider);
    }
}
