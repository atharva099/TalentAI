package com.talentai.dto.response;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CandidateResponse {
    Long id;
    String firstName;
    String lastName;
    String email;
    String phone;
    String skills;
    Integer experienceYears;
}
