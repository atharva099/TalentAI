package com.talentai.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateRequest {

    @NotBlank(message = "First name is required.")
    @Size(max = 100, message = "First name must not exceed 100 characters.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    @Size(max = 100, message = "Last name must not exceed 100 characters.")
    private String lastName;

    @NotBlank(message = "Email is required.")
    @Email(message = "Email must be a valid email address.")
    @Size(max = 255, message = "Email must not exceed 255 characters.")
    private String email;

    @Size(max = 30, message = "Phone must not exceed 30 characters.")
    private String phone;

    @Size(max = 2000, message = "Skills must not exceed 2000 characters.")
    private String skills;

    @Min(value = 0, message = "Experience years must not be negative.")
    @Max(value = 100, message = "Experience years must not exceed 100.")
    private Integer experienceYears;
}
