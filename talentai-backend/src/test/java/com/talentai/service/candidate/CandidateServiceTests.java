package com.talentai.service.candidate;

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

import com.talentai.dto.request.CandidateRequest;
import com.talentai.entity.Candidate;
import com.talentai.exception.ApplicationException;
import com.talentai.exception.ErrorCode;
import com.talentai.repository.CandidateRepository;

@ExtendWith(MockitoExtension.class)
class CandidateServiceTests {

    @Mock
    private CandidateRepository candidateRepository;

    @InjectMocks
    private CandidateServiceImpl candidateService;

    @Test
    void createCandidateNormalizesEmail() {
        CandidateRequest request = request(" Candidate@Example.com ");
        when(candidateRepository.save(any(Candidate.class))).thenAnswer(invocation -> {
            Candidate candidate = invocation.getArgument(0);
            candidate.setId(1L);
            return candidate;
        });

        assertEquals("candidate@example.com", candidateService.createCandidate(request).getEmail());
    }

    @Test
    void duplicateCandidateEmailIsRejected() {
        when(candidateRepository.existsByEmail("candidate@example.com")).thenReturn(true);

        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> candidateService.createCandidate(request("candidate@example.com")));

        assertEquals(ErrorCode.CANDIDATE_ALREADY_EXISTS, exception.getErrorCode());
    }

    @Test
    void missingCandidateIsRejected() {
        when(candidateRepository.findById(99L)).thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> candidateService.getCandidateById(99L));

        assertEquals(ErrorCode.CANDIDATE_NOT_FOUND, exception.getErrorCode());
    }

    private CandidateRequest request(String email) {
        return CandidateRequest.builder()
                .firstName("Ada")
                .lastName("Lovelace")
                .email(email)
                .skills("Java")
                .experienceYears(5)
                .build();
    }
}
