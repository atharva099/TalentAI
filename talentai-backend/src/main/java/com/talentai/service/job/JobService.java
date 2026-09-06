package com.talentai.service.job;

import java.util.List;

import com.talentai.dto.request.JobRequest;
import com.talentai.dto.response.JobResponse;

/**
 * Defines job opening lifecycle operations.
 */
public interface JobService {

    JobResponse createJob(JobRequest request);

    List<JobResponse> getAllJobs();

    JobResponse getJobById(Long jobId);

    JobResponse updateJob(Long jobId, JobRequest request);

    void deleteJob(Long jobId);
}
