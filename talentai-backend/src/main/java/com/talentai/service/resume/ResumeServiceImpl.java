package com.talentai.service.resume;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.talentai.dto.request.ResumeRequest;
import com.talentai.dto.response.ResumeResponse;
import com.talentai.entity.Candidate;
import com.talentai.entity.Resume;
import com.talentai.exception.ApplicationException;
import com.talentai.exception.ErrorCode;
import com.talentai.repository.CandidateRepository;
import com.talentai.repository.ResumeRepository;

import lombok.RequiredArgsConstructor;

/**
 * Implements resume metadata persistence operations.
 */
@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepository;
    private final CandidateRepository candidateRepository;

    @Override
    @Transactional
    public ResumeResponse createResume(ResumeRequest request) {
        Candidate candidate = candidateRepository.findById(request.getCandidateId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.CANDIDATE_NOT_FOUND));

        Resume resume = Resume.builder()
                .candidate(candidate)
                .fileName(request.getFileName())
                .contentType(request.getContentType())
                .fileSizeBytes(request.getFileSizeBytes())
                .build();

        return ResumeMapper.toResponse(resumeRepository.save(resume));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResumeResponse> getAllResumes() {
        return resumeRepository.findAll().stream().map(ResumeMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ResumeResponse getResumeById(Long resumeId) {
        return resumeRepository.findById(resumeId)
                .map(ResumeMapper::toResponse)
                .orElseThrow(() -> new ApplicationException(ErrorCode.RESUME_NOT_FOUND));
    }

    @Override
    @Transactional
    public void deleteResume(Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.RESUME_NOT_FOUND));
        resumeRepository.delete(resume);
    }
}
