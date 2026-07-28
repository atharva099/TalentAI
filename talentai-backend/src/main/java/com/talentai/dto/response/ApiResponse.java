package com.talentai.dto.response;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Value;

/**
 * Standard successful API response envelope used by all TalentAI REST endpoints.
 *
 * @param <T> the type of response payload
 */
@Value
@Builder
public class ApiResponse<T> {

    boolean success;
    String message;
    T data;

    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    Instant timestamp = Instant.now();

    String requestId;

    /**
     * Creates a successful response with a payload.
     *
     * @param message human-readable result message
     * @param data response payload
     * @param requestId correlation identifier for the request
     * @param <T> the payload type
     * @return a successful API response
     */
    public static <T> ApiResponse<T> success(String message, T data, String requestId) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .requestId(requestId)
                .build();
    }
}
