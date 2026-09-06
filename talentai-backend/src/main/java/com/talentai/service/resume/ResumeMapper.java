package com.talentai.service.resume;

import com.talentai.dto.response.ResumeResponse;
import com.talentai.entity.Resume;

/**
 * Maps Resume entities to API responses.
 */
public final class ResumeMapper {

    private ResumeMapper() {
    }

    public static ResumeResponse toResponse(Resume resume) {
        return ResumeResponse.builder()
                .id(resume.getId())
                .candidateId(resume.getCandidate().getId())
                .fileName(resume.getFileName())
                .contentType(resume.getContentType())
                .fileSizeBytes(resume.getFileSizeBytes())
                .build();
    }
}
