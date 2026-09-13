package com.talentai.controller.auth;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.talentai.entity.Role;
import com.talentai.entity.User;
import com.talentai.repository.RoleRepository;
import com.talentai.repository.UserRepository;
import com.talentai.security.UserRole;

@SpringBootTest
@ActiveProfiles("test")
class CurrentUserAuthorizationTests {

    private static final String USER_EMAIL = "current-user@example.com";

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        userRepository.deleteAll();
        Role candidateRole = roleRepository.findByName(UserRole.CANDIDATE).orElseThrow();
        userRepository.save(User.builder()
                .firstName("Current")
                .lastName("User")
                .email(USER_EMAIL)
                .password("hashed-password")
                .roles(Set.of(candidateRole))
                .build());
    }

    @Test
    @WithMockUser(username = USER_EMAIL, roles = "CANDIDATE")
    void authenticatedUserCanAccessCurrentUserEndpoint() throws Exception {
        mockMvc.perform(get("/api/v1/auth/me"))
                .andExpect(status().isOk());
    }

    @Test
    void unauthenticatedRequestIsRejected() throws Exception {
        mockMvc.perform(get("/api/v1/auth/me"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = USER_EMAIL, roles = "CANDIDATE")
    void responseContainsPersistedRolesWithoutPassword() throws Exception {
        mockMvc.perform(get("/api/v1/auth/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").isNumber())
                .andExpect(jsonPath("$.data.firstName").value("Current"))
                .andExpect(jsonPath("$.data.lastName").value("User"))
                .andExpect(jsonPath("$.data.email").value(USER_EMAIL))
                .andExpect(jsonPath("$.data.roles[0]").value("CANDIDATE"))
                .andExpect(jsonPath("$.data.password").doesNotExist())
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("hashed-password"))));
    }
}
