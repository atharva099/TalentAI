package com.talentai.exception;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

/**
 * Standard error response envelope returned by TalentAI APIs.
 *
 * <p>Validation errors are keyed by request field name and can contain multiple
 * messages for a field.</p>
 */
@Value
@Builder
public class ApiErrorResponse {

    @Builder.Default
    boolean success = false;

    ErrorCode errorCode;
    String message;

    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    Instant timestamp = Instant.now();

    String requestId;

    @Singular("validationError")
    Map<String, List<String>> validationErrors;

    /**
     * Creates a standard error response using the error code's default message.
     *
     * @param errorCode stable application error code
     * @param requestId correlation identifier for the request
     * @return an API error response
     */
    public static ApiErrorResponse of(ErrorCode errorCode, String requestId) {
        return ApiErrorResponse.builder()
                .errorCode(errorCode)
                .message(errorCode.getDefaultMessage())
                .requestId(requestId)
                .build();
    }
}
