package com.talentai.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Resume metadata payload. The binary file is handled outside this milestone.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeRequest {

    @NotNull(message = "Candidate ID is required.")
    @Positive(message = "Candidate ID must be positive.")
    private Long candidateId;

    @NotBlank(message = "File name is required.")
    @Size(max = 255, message = "File name must not exceed 255 characters.")
    private String fileName;

    @NotBlank(message = "Content type is required.")
    @Size(max = 100, message = "Content type must not exceed 100 characters.")
    private String contentType;

    @NotNull(message = "File size is required.")
    @Positive(message = "File size must be positive.")
    private Long fileSizeBytes;
}
