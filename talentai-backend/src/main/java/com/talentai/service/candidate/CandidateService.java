package com.talentai.service.candidate;

import java.util.List;

import com.talentai.dto.request.CandidateRequest;
import com.talentai.dto.response.CandidateResponse;

public interface CandidateService {
    CandidateResponse createCandidate(CandidateRequest request);
    CandidateResponse getCandidateById(Long candidateId);
    List<CandidateResponse> getAllCandidates();
    CandidateResponse updateCandidate(Long candidateId, CandidateRequest request);
    void deleteCandidate(Long candidateId);
}
