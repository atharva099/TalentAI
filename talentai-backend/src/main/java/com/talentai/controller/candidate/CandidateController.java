package com.talentai.controller.candidate;

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

import com.talentai.dto.request.CandidateRequest;
import com.talentai.dto.response.ApiResponse;
import com.talentai.dto.response.CandidateResponse;
import com.talentai.service.candidate.CandidateService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/candidates")
@RequiredArgsConstructor
@Validated
public class CandidateController {

    private static final String REQUEST_ID_HEADER = "X-Request-Id";
    private final CandidateService candidateService;

    @PostMapping
    public ResponseEntity<ApiResponse<CandidateResponse>> createCandidate(
            @Valid @RequestBody CandidateRequest candidateRequest, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Candidate created successfully.",
                candidateService.createCandidate(candidateRequest), requestId(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CandidateResponse>>> getAllCandidates(HttpServletRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Candidates retrieved successfully.",
                candidateService.getAllCandidates(), requestId(request)));
    }

    @GetMapping("/{candidateId}")
    public ResponseEntity<ApiResponse<CandidateResponse>> getCandidateById(
            @PathVariable @Positive(message = "Candidate ID must be positive.") Long candidateId,
            HttpServletRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Candidate retrieved successfully.",
                candidateService.getCandidateById(candidateId), requestId(request)));
    }

    @PutMapping("/{candidateId}")
    public ResponseEntity<ApiResponse<CandidateResponse>> updateCandidate(
            @PathVariable @Positive(message = "Candidate ID must be positive.") Long candidateId,
            @Valid @RequestBody CandidateRequest candidateRequest, HttpServletRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Candidate updated successfully.",
                candidateService.updateCandidate(candidateId, candidateRequest), requestId(request)));
    }

    @DeleteMapping("/{candidateId}")
    public ResponseEntity<ApiResponse<Void>> deleteCandidate(
            @PathVariable @Positive(message = "Candidate ID must be positive.") Long candidateId,
            HttpServletRequest request) {
        candidateService.deleteCandidate(candidateId);
        return ResponseEntity.ok(ApiResponse.success("Candidate deleted successfully.", null, requestId(request)));
    }

    private String requestId(HttpServletRequest request) {
        return request.getHeader(REQUEST_ID_HEADER);
    }
}
