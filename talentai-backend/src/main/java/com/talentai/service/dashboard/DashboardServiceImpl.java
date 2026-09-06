package com.talentai.service.dashboard;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.talentai.dto.response.DashboardResponse;
import com.talentai.repository.CandidateRepository;
import com.talentai.repository.JobRepository;
import com.talentai.repository.ResumeRepository;

import lombok.RequiredArgsConstructor;

/**
 * Builds dashboard summaries from existing persisted module data.
 */
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final CandidateRepository candidateRepository;
    private final JobRepository jobRepository;
    private final ResumeRepository resumeRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardResponse getSummary() {
        return DashboardResponse.builder()
                .candidateCount(candidateRepository.count())
                .jobCount(jobRepository.count())
                .resumeCount(resumeRepository.count())
                .build();
    }
}
