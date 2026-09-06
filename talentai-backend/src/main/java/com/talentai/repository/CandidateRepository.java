package com.talentai.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.talentai.entity.Candidate;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    boolean existsByEmail(String email);
}
