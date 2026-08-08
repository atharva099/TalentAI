package com.talentai;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.talentai.config.RoleInitializer;
import com.talentai.dto.request.UserRequest;
import com.talentai.entity.User;
import com.talentai.repository.RoleRepository;
import com.talentai.repository.UserRepository;
import com.talentai.security.UserRole;
import com.talentai.service.auth.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AuthenticationRoleAssignmentTests {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleInitializer roleInitializer;

    private BCryptPasswordEncoder passwordEncoder;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldInitializeAllReferenceRolesOnStartup() {
        assertThat(roleRepository.count()).isEqualTo(UserRole.values().length);

        for (UserRole role : UserRole.values()) {
            assertThat(roleRepository.findByName(role)).isPresent();
        }
    }

    @Test
    void shouldNotDuplicateRolesWhenInitializerRunsMultipleTimes() {
        roleInitializer.initializeRoles();
        roleInitializer.initializeRoles();

        assertThat(roleRepository.count()).isEqualTo(UserRole.values().length);
    }

    @Test
    void shouldAssignCandidateRoleToNewlyRegisteredUser() {
        UserRequest request = UserRequest.builder()
                .firstName("Candidate")
                .lastName("User")
                .email("candidate@example.com")
                .password("Password123")
                .build();

        authenticationService.register(request);

        User savedUser = userRepository.findByEmail("candidate@example.com")
                .orElseThrow();

        assertThat(savedUser.getRoles()).hasSize(1);
        assertThat(savedUser.getRoles()).extracting("name").containsExactly(UserRole.CANDIDATE);
        assertThat(passwordEncoder.matches("Password123", savedUser.getPassword())).isTrue();
    }

    @Test
    void shouldRejectClientSuppliedRoleFieldWhenDeserializingRegistrationPayload() throws Exception {
        java.util.Map<String, Object> requestBodyMap = new java.util.HashMap<>();
        requestBodyMap.put("firstName", "Malicious");
        requestBodyMap.put("lastName", "User");
        requestBodyMap.put("email", "malicious@example.com");
        requestBodyMap.put("password", "Password123");
        requestBodyMap.put("role", "PLATFORM_ADMIN");

        String requestBody = objectMapper.writeValueAsString(requestBodyMap);

        assertThatThrownBy(() -> objectMapper.readValue(requestBody, UserRequest.class))
                .isInstanceOf(com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException.class);
    }
}
