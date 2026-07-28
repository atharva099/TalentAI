package com.talentai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Provides the password encoder used by future authentication workflows.
 */
@Configuration
public class PasswordConfig {

    /**
     * Creates a BCrypt password encoder for securely hashing user passwords.
     *
     * @return BCrypt password encoder
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
