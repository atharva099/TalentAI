package com.talentai.dto.response;

import java.util.Set;

import com.talentai.security.UserRole;

import lombok.Builder;
import lombok.Value;

/**
 * Safe representation of the currently authenticated user.
 */
@Value
@Builder
public class CurrentUserResponse {

    Long id;

    String firstName;

    String lastName;

    String email;

    Set<UserRole> roles;
}
