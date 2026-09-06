package com.talentai.dto.response;

import lombok.Builder;
import lombok.Value;

/**
 * Safe resume metadata representation.
 */
@Value
@Builder
public class ResumeResponse {

    Long id;
    Long candidateId;
    String fileName;
    String contentType;
    Long fileSizeBytes;
}
