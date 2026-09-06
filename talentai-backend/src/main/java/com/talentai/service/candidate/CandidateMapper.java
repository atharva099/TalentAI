package com.talentai.service.candidate;

import com.talentai.dto.request.CandidateRequest;
import com.talentai.dto.response.CandidateResponse;
import com.talentai.entity.Candidate;

public final class CandidateMapper {

    private CandidateMapper() {
    }

    public static Candidate toEntity(CandidateRequest request) {
        return Candidate.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .skills(request.getSkills())
                .experienceYears(request.getExperienceYears())
                .build();
    }

    public static CandidateResponse toResponse(Candidate candidate) {
        return CandidateResponse.builder()
                .id(candidate.getId())
                .firstName(candidate.getFirstName())
                .lastName(candidate.getLastName())
                .email(candidate.getEmail())
                .phone(candidate.getPhone())
                .skills(candidate.getSkills())
                .experienceYears(candidate.getExperienceYears())
                .build();
    }

    public static void updateEntity(CandidateRequest request, Candidate candidate) {
        candidate.setFirstName(request.getFirstName());
        candidate.setLastName(request.getLastName());
        candidate.setEmail(request.getEmail());
        candidate.setPhone(request.getPhone());
        candidate.setSkills(request.getSkills());
        candidate.setExperienceYears(request.getExperienceYears());
    }
}
