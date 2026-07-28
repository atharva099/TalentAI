package com.talentai.dto.response;

import lombok.Builder;
import lombok.Value;

/**
 * Safe user representation returned by TalentAI APIs.
 *
 * <p>Authentication secrets, including the password hash, are intentionally excluded.</p>
 */
@Value
@Builder
public class UserResponse {

    Long id;

    String firstName;

    String lastName;

    String email;

}
