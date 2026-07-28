package com.talentai.security;

import java.util.Locale;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.talentai.entity.User;
import com.talentai.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Loads persisted TalentAI users for Spring Security credential and JWT authentication.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Loads a user by normalized email address.
     *
     * @param email user email address
     * @return Spring Security user details
     * @throws UsernameNotFoundException when no matching user exists
     */
    @Override
    public UserDetails loadUserByUsername(String email) {
        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(UserRole.CANDIDATE.name())
                .build();
    }
}
