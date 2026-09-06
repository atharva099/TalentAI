package com.talentai.service.resume;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.talentai.dto.request.ResumeRequest;
import com.talentai.dto.response.ResumeResponse;
import com.talentai.entity.Candidate;
import com.talentai.entity.Resume;
import com.talentai.exception.ApplicationException;
import com.talentai.exception.ErrorCode;
import com.talentai.repository.CandidateRepository;
import com.talentai.repository.ResumeRepository;

@ExtendWith(MockitoExtension.class)
class ResumeServiceTests {

    @Mock
    private ResumeRepository resumeRepository;

    @Mock
    private CandidateRepository candidateRepository;

    @InjectMocks
    private ResumeServiceImpl resumeService;

    @Test
    void createsResumeForExistingCandidate() {
        Candidate candidate = Candidate.builder().id(1L).build();
        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
        when(resumeRepository.save(any(Resume.class))).thenAnswer(invocation -> {
            Resume resume = invocation.getArgument(0);
            resume.setId(2L);
            return resume;
        });

        ResumeResponse response = resumeService.createResume(request());

        assertEquals(2L, response.getId());
        assertEquals(1L, response.getCandidateId());
    }

    @Test
    void rejectsResumeForMissingCandidate() {
        when(candidateRepository.findById(99L)).thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> resumeService.createResume(ResumeRequest.builder()
                        .candidateId(99L)
                        .fileName("resume.pdf")
                        .contentType("application/pdf")
                        .fileSizeBytes(100L)
                        .build()));

        assertEquals(ErrorCode.CANDIDATE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void rejectsMissingResume() {
        when(resumeRepository.findById(99L)).thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> resumeService.getResumeById(99L));

        assertEquals(ErrorCode.RESUME_NOT_FOUND, exception.getErrorCode());
    }

    private ResumeRequest request() {
        return ResumeRequest.builder()
                .candidateId(1L)
                .fileName("resume.pdf")
                .contentType("application/pdf")
                .fileSizeBytes(100L)
                .build();
    }
}
