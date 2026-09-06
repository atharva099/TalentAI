package com.talentai.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request payload used to create or update a job opening.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobRequest {

    @NotBlank(message = "Job title is required.")
    @Size(max = 200, message = "Job title must not exceed 200 characters.")
    private String title;

    @NotBlank(message = "Job description is required.")
    @Size(max = 5000, message = "Job description must not exceed 5000 characters.")
    private String description;

    @NotBlank(message = "Job location is required.")
    @Size(max = 200, message = "Job location must not exceed 200 characters.")
    private String location;

    @NotBlank(message = "Employment type is required.")
    @Size(max = 100, message = "Employment type must not exceed 100 characters.")
    private String employmentType;
}
