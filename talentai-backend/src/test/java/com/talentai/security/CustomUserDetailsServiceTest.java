package com.talentai.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import com.talentai.entity.Role;
import com.talentai.entity.User;
import com.talentai.repository.UserRepository;
import com.talentai.security.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    private CustomUserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userDetailsService = new CustomUserDetailsService(userRepository);
    }

    @Test
    void shouldMapPersistedRolesToGrantedAuthorities() {
        User user = User.builder()
                .email("candidate@example.com")
                .password("encoded-password")
                .roles(Set.of(
                        Role.builder().name(UserRole.CANDIDATE).build(),
                        Role.builder().name(UserRole.PLATFORM_ADMIN).build()))
                .build();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername("candidate@example.com");

        assertThat(userDetails.getAuthorities()).extracting("authority")
                .containsExactlyInAnyOrder("ROLE_CANDIDATE", "ROLE_PLATFORM_ADMIN");
    }

    @Test
    void shouldNotHardcodeCandidateWhenPersistedRoleIsRecruiter() {
        User user = User.builder()
                .email("recruiter@example.com")
                .password("encoded-password")
                .roles(Set.of(Role.builder().name(UserRole.RECRUITER).build()))
                .build();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername("recruiter@example.com");

        assertThat(userDetails.getAuthorities()).extracting("authority")
                .containsExactly("ROLE_RECRUITER");
    }

    @Test
    void shouldReturnEmptyAuthoritiesWhenUserHasNoRoles() {
        User user = User.builder()
                .email("norole@example.com")
                .password("encoded-password")
                .roles(Set.of())
                .build();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername("norole@example.com");

        assertThat(userDetails.getAuthorities()).isEmpty();
    }
}
