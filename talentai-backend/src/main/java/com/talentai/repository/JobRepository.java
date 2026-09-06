package com.talentai.repository;

import com.talentai.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for persisted job openings.
 */
public interface JobRepository extends JpaRepository<Job, Long> {
}
