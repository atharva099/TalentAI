package com.talentai.exception;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

/**
 * Converts application and validation failures into TalentAI's standard API error response.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String REQUEST_ID_HEADER = "X-Request-Id";

    /**
     * Handles expected application failures raised by service-layer business rules.
     *
     * @param exception typed application exception
     * @param request current HTTP request
     * @return standardized API error response
     */
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApiErrorResponse> handleApplicationException(
            ApplicationException exception,
            HttpServletRequest request) {
        return buildResponse(exception.getErrorCode(), resolveRequestId(request), null);
    }

    /**
     * Handles invalid request DTO fields detected by Jakarta Validation.
     *
     * @param exception request-body validation exception
     * @param request current HTTP request
     * @return standardized validation error response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {
        Map<String, List<String>> validationErrors = new LinkedHashMap<>();

        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            validationErrors
                    .computeIfAbsent(fieldError.getField(), ignored -> new ArrayList<>())
                    .add(fieldError.getDefaultMessage());
        }

        return buildResponse(ErrorCode.REQUEST_VALIDATION_FAILED, resolveRequestId(request), validationErrors);
    }

    /**
     * Handles validation failures raised for method parameters and path/query constraints.
     *
     * @param exception constraint validation exception
     * @param request current HTTP request
     * @return standardized validation error response
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolationException(
            ConstraintViolationException exception,
            HttpServletRequest request) {
        Map<String, List<String>> validationErrors = new LinkedHashMap<>();

        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            String fieldName = extractFieldName(violation);
            validationErrors
                    .computeIfAbsent(fieldName, ignored -> new ArrayList<>())
                    .add(violation.getMessage());
        }

        return buildResponse(ErrorCode.REQUEST_VALIDATION_FAILED, resolveRequestId(request), validationErrors);
    }

    /**
     * Handles unexpected failures without exposing internal implementation details to clients.
     *
     * @param exception unexpected exception
     * @param request current HTTP request
     * @return standardized internal error response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpectedException(
            Exception exception,
            HttpServletRequest request) {
        String requestId = resolveRequestId(request);
        LOGGER.error("Unhandled application exception. requestId={}", requestId, exception);

        return buildResponse(ErrorCode.SYSTEM_INTERNAL_ERROR, requestId, null);
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(
            ErrorCode errorCode,
            String requestId,
            Map<String, List<String>> validationErrors) {
        ApiErrorResponse response = ApiErrorResponse.builder()
                .errorCode(errorCode)
                .message(errorCode.getDefaultMessage())
                .requestId(requestId)
                .validationErrors(validationErrors)
                .build();

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    private String resolveRequestId(HttpServletRequest request) {
        String requestId = request.getHeader(REQUEST_ID_HEADER);
        return requestId == null || requestId.isBlank() ? UUID.randomUUID().toString() : requestId;
    }

    private String extractFieldName(ConstraintViolation<?> violation) {
        String propertyPath = violation.getPropertyPath().toString();
        int lastSeparatorIndex = propertyPath.lastIndexOf('.');

        return lastSeparatorIndex >= 0
                ? propertyPath.substring(lastSeparatorIndex + 1)
                : propertyPath;
    }
}
