package com.talentai.security;

/**
 * Defines the system roles recognized by TalentAI authorization rules.
 *
 * <p>Additional roles can be introduced as product authorization requirements evolve.</p>
 */
public enum UserRole {

    CANDIDATE,
    RECRUITER,
    COMPANY_ADMIN,
    PLATFORM_ADMIN
}
