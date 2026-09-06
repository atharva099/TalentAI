package com.talentai.service.job;

import com.talentai.dto.request.JobRequest;
import com.talentai.dto.response.JobResponse;
import com.talentai.entity.Job;

/**
 * Maps Job API DTOs to and from the persistence entity.
 */
public final class JobMapper {

    private JobMapper() {
    }

    public static Job toEntity(JobRequest request) {
        return Job.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .location(request.getLocation())
                .employmentType(request.getEmploymentType())
                .build();
    }

    public static JobResponse toResponse(Job job) {
        return JobResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .location(job.getLocation())
                .employmentType(job.getEmploymentType())
                .build();
    }

    public static void updateEntity(JobRequest request, Job job) {
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setLocation(request.getLocation());
        job.setEmploymentType(request.getEmploymentType());
    }
}
