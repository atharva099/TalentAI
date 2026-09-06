package com.talentai.service.resume;

import java.util.List;

import com.talentai.dto.request.ResumeRequest;
import com.talentai.dto.response.ResumeResponse;

/**
 * Defines resume metadata lifecycle operations.
 */
public interface ResumeService {

    ResumeResponse createResume(ResumeRequest request);

    List<ResumeResponse> getAllResumes();

    ResumeResponse getResumeById(Long resumeId);

    void deleteResume(Long resumeId);
}
