package com.talentai.repository;

import com.talentai.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for persisted resume metadata.
 */
public interface ResumeRepository extends JpaRepository<Resume, Long> {
}
