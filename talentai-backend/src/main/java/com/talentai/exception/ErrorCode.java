package com.talentai.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Stable application error codes returned by TalentAI APIs.
 *
 * <p>New codes should be added without changing the meaning of existing values,
 * because API clients can rely on them for programmatic error handling.</p>
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found."),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "A user with the supplied details already exists."),

    AUTH_INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "The supplied credentials are invalid."),
    AUTH_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Authentication is required."),
    AUTH_FORBIDDEN(HttpStatus.FORBIDDEN, "You do not have permission to perform this action."),

    REQUEST_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "One or more fields are invalid."),
    REQUEST_INVALID(HttpStatus.BAD_REQUEST, "The request is invalid."),

    CANDIDATE_NOT_FOUND(HttpStatus.NOT_FOUND, "Candidate not found."),
    COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND, "Company not found."),
    JOB_NOT_FOUND(HttpStatus.NOT_FOUND, "Job not found."),
    RESUME_NOT_FOUND(HttpStatus.NOT_FOUND, "Resume not found."),
    APPLICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "Application not found."),
    INTERVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "Interview not found."),

    APPLICATION_INVALID_STATUS_TRANSITION(
            HttpStatus.UNPROCESSABLE_CONTENT,
            "The application cannot transition to the requested status."),
    RESOURCE_STATE_CONFLICT(HttpStatus.CONFLICT, "The request conflicts with the current resource state."),

    FILE_PROCESSING_FAILED(HttpStatus.UNPROCESSABLE_CONTENT, "The file could not be processed."),
    AI_PROCESSING_FAILED(HttpStatus.BAD_GATEWAY, "The AI service could not process the request."),
    INTEGRATION_SERVICE_UNAVAILABLE(HttpStatus.BAD_GATEWAY, "A required external service is unavailable."),
    SYSTEM_INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.");

    private final HttpStatus httpStatus;
    private final String defaultMessage;
}
