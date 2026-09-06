package com.talentai.service.job;

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

import com.talentai.dto.request.JobRequest;
import com.talentai.entity.Job;
import com.talentai.exception.ApplicationException;
import com.talentai.exception.ErrorCode;
import com.talentai.repository.JobRepository;

@ExtendWith(MockitoExtension.class)
class JobServiceTests {

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private JobServiceImpl jobService;

    @Test
    void createsJob() {
        when(jobRepository.save(any(Job.class))).thenAnswer(invocation -> {
            Job job = invocation.getArgument(0);
            job.setId(1L);
            return job;
        });

        assertEquals("Backend Engineer", jobService.createJob(request()).getTitle());
    }

    @Test
    void missingJobIsRejected() {
        when(jobRepository.findById(99L)).thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> jobService.getJobById(99L));

        assertEquals(ErrorCode.JOB_NOT_FOUND, exception.getErrorCode());
    }

    private JobRequest request() {
        return JobRequest.builder()
                .title("Backend Engineer")
                .description("Build backend services.")
                .location("Remote")
                .employmentType("FULL_TIME")
                .build();
    }
}
