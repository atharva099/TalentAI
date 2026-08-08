package com.talentai.repository;

import com.talentai.entity.Role;
import com.talentai.security.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for role persistence operations.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Finds a role by its enum name.
     *
     * @param name the role name
     * @return optional role entity
     */
    Optional<Role> findByName(UserRole name);
}
