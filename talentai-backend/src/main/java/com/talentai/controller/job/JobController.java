package com.talentai.controller.job;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.talentai.dto.request.JobRequest;
import com.talentai.dto.response.ApiResponse;
import com.talentai.dto.response.JobResponse;
import com.talentai.service.job.JobService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

/**
 * Exposes CRUD endpoints for recruiter-managed job openings.
 */
@RestController
@RequestMapping("/api/v1/jobs")
@RequiredArgsConstructor
@Validated
public class JobController {

    private static final String REQUEST_ID_HEADER = "X-Request-Id";

    private final JobService jobService;

    @PostMapping
    public ResponseEntity<ApiResponse<JobResponse>> createJob(
            @Valid @RequestBody JobRequest request, HttpServletRequest httpRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Job created successfully.", jobService.createJob(request),
                        resolveRequestId(httpRequest)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<JobResponse>>> getAllJobs(HttpServletRequest httpRequest) {
        return ResponseEntity.ok(ApiResponse.success("Jobs retrieved successfully.", jobService.getAllJobs(),
                resolveRequestId(httpRequest)));
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<ApiResponse<JobResponse>> getJobById(
            @PathVariable @Positive(message = "Job ID must be positive.") Long jobId,
            HttpServletRequest httpRequest) {
        return ResponseEntity.ok(ApiResponse.success("Job retrieved successfully.", jobService.getJobById(jobId),
                resolveRequestId(httpRequest)));
    }

    @PutMapping("/{jobId}")
    public ResponseEntity<ApiResponse<JobResponse>> updateJob(
            @PathVariable @Positive(message = "Job ID must be positive.") Long jobId,
            @Valid @RequestBody JobRequest request, HttpServletRequest httpRequest) {
        return ResponseEntity.ok(ApiResponse.success("Job updated successfully.",
                jobService.updateJob(jobId, request), resolveRequestId(httpRequest)));
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<ApiResponse<Void>> deleteJob(
            @PathVariable @Positive(message = "Job ID must be positive.") Long jobId,
            HttpServletRequest httpRequest) {
        jobService.deleteJob(jobId);
        return ResponseEntity.ok(ApiResponse.success("Job deleted successfully.", null,
                resolveRequestId(httpRequest)));
    }

    private String resolveRequestId(HttpServletRequest request) {
        return request.getHeader(REQUEST_ID_HEADER);
    }
}
