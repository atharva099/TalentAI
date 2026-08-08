package com.talentai.config;

import com.talentai.entity.Role;
import com.talentai.repository.RoleRepository;
import com.talentai.security.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Ensures the system reference roles exist before the application begins handling requests.
 */
@Component
@RequiredArgsConstructor
public class RoleInitializer implements ApplicationRunner {

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        initializeRoles();
    }

    /**
     * Ensures each configured system role exists in the database.
     *
     * <p>This method is idempotent and safe to run multiple times.</p>
     */
    public void initializeRoles() {
        for (UserRole userRole : UserRole.values()) {
            if (roleRepository.findByName(userRole).isEmpty()) {
                roleRepository.save(Role.builder().name(userRole).build());
            }
        }
    }
}
