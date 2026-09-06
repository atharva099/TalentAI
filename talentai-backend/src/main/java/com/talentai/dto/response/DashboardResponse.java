package com.talentai.dto.response;

import lombok.Builder;
import lombok.Value;

/**
 * Summary counts for the TalentAI recruiter dashboard.
 */
@Value
@Builder
public class DashboardResponse {

    long candidateCount;
    long jobCount;
    long resumeCount;
}
