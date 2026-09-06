package com.talentai.service.job;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.talentai.dto.request.JobRequest;
import com.talentai.dto.response.JobResponse;
import com.talentai.entity.Job;
import com.talentai.exception.ApplicationException;
import com.talentai.exception.ErrorCode;
import com.talentai.repository.JobRepository;

import lombok.RequiredArgsConstructor;

/**
 * Implements job opening persistence operations.
 */
@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;

    @Override
    @Transactional
    public JobResponse createJob(JobRequest request) {
        return JobMapper.toResponse(jobRepository.save(JobMapper.toEntity(request)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobResponse> getAllJobs() {
        return jobRepository.findAll().stream().map(JobMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public JobResponse getJobById(Long jobId) {
        return jobRepository.findById(jobId)
                .map(JobMapper::toResponse)
                .orElseThrow(() -> new ApplicationException(ErrorCode.JOB_NOT_FOUND));
    }

    @Override
    @Transactional
    public JobResponse updateJob(Long jobId, JobRequest request) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.JOB_NOT_FOUND));
        JobMapper.updateEntity(request, job);
        return JobMapper.toResponse(jobRepository.save(job));
    }

    @Override
    @Transactional
    public void deleteJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.JOB_NOT_FOUND));
        jobRepository.delete(job);
    }
}
