package com.talentai.service.dashboard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.talentai.repository.CandidateRepository;
import com.talentai.repository.JobRepository;
import com.talentai.repository.ResumeRepository;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTests {

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private ResumeRepository resumeRepository;

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    @Test
    void returnsSummaryCounts() {
        when(candidateRepository.count()).thenReturn(4L);
        when(jobRepository.count()).thenReturn(2L);
        when(resumeRepository.count()).thenReturn(3L);

        var response = dashboardService.getSummary();

        assertEquals(4L, response.getCandidateCount());
        assertEquals(2L, response.getJobCount());
        assertEquals(3L, response.getResumeCount());
    }
}
