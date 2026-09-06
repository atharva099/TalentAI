package com.talentai.service.candidate;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.talentai.dto.request.CandidateRequest;
import com.talentai.dto.response.CandidateResponse;
import com.talentai.entity.Candidate;
import com.talentai.exception.ApplicationException;
import com.talentai.exception.ErrorCode;
import com.talentai.repository.CandidateRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;

    @Override
    @Transactional
    public CandidateResponse createCandidate(CandidateRequest request) {
        String email = normalizeEmail(request.getEmail());
        if (candidateRepository.existsByEmail(email)) {
            throw new ApplicationException(ErrorCode.CANDIDATE_ALREADY_EXISTS);
        }
        Candidate candidate = CandidateMapper.toEntity(request);
        candidate.setEmail(email);
        return CandidateMapper.toResponse(candidateRepository.save(candidate));
    }

    @Override
    @Transactional(readOnly = true)
    public CandidateResponse getCandidateById(Long candidateId) {
        return candidateRepository.findById(candidateId)
                .map(CandidateMapper::toResponse)
                .orElseThrow(() -> new ApplicationException(ErrorCode.CANDIDATE_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CandidateResponse> getAllCandidates() {
        return candidateRepository.findAll().stream().map(CandidateMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public CandidateResponse updateCandidate(Long candidateId, CandidateRequest request) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.CANDIDATE_NOT_FOUND));
        String email = normalizeEmail(request.getEmail());
        if (!candidate.getEmail().equals(email) && candidateRepository.existsByEmail(email)) {
            throw new ApplicationException(ErrorCode.CANDIDATE_ALREADY_EXISTS);
        }
        CandidateMapper.updateEntity(request, candidate);
        candidate.setEmail(email);
        return CandidateMapper.toResponse(candidateRepository.save(candidate));
    }

    @Override
    @Transactional
    public void deleteCandidate(Long candidateId) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.CANDIDATE_NOT_FOUND));
        candidateRepository.delete(candidate);
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
