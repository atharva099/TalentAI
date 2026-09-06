package com.talentai.dto.response;

import lombok.Builder;
import lombok.Value;

/**
 * Safe representation of a job opening.
 */
@Value
@Builder
public class JobResponse {

    Long id;
    String title;
    String description;
    String location;
    String employmentType;
}
