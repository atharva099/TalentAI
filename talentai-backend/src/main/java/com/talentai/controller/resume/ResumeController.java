package com.talentai.controller.resume;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.talentai.dto.request.ResumeRequest;
import com.talentai.dto.response.ApiResponse;
import com.talentai.dto.response.ResumeResponse;
import com.talentai.service.resume.ResumeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

/**
 * Exposes resume metadata endpoints.
 */
@RestController
@RequestMapping("/api/v1/resumes")
@RequiredArgsConstructor
@Validated
public class ResumeController {

    private static final String REQUEST_ID_HEADER = "X-Request-Id";

    private final ResumeService resumeService;

    @PostMapping
    public ResponseEntity<ApiResponse<ResumeResponse>> createResume(
            @Valid @RequestBody ResumeRequest request, HttpServletRequest httpRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Resume created successfully.", resumeService.createResume(request),
                        resolveRequestId(httpRequest)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ResumeResponse>>> getAllResumes(HttpServletRequest httpRequest) {
        return ResponseEntity.ok(ApiResponse.success("Resumes retrieved successfully.",
                resumeService.getAllResumes(), resolveRequestId(httpRequest)));
    }

    @GetMapping("/{resumeId}")
    public ResponseEntity<ApiResponse<ResumeResponse>> getResumeById(
            @PathVariable @Positive(message = "Resume ID must be positive.") Long resumeId,
            HttpServletRequest httpRequest) {
        return ResponseEntity.ok(ApiResponse.success("Resume retrieved successfully.",
                resumeService.getResumeById(resumeId), resolveRequestId(httpRequest)));
    }

    @DeleteMapping("/{resumeId}")
    public ResponseEntity<ApiResponse<Void>> deleteResume(
            @PathVariable @Positive(message = "Resume ID must be positive.") Long resumeId,
            HttpServletRequest httpRequest) {
        resumeService.deleteResume(resumeId);
        return ResponseEntity.ok(ApiResponse.success("Resume deleted successfully.", null,
                resolveRequestId(httpRequest)));
    }

    private String resolveRequestId(HttpServletRequest request) {
        return request.getHeader(REQUEST_ID_HEADER);
    }
}
