package com.talentai.exception;

import lombok.Getter;

/**
 * Runtime exception carrying a stable TalentAI application error code.
 */
@Getter
public class ApplicationException extends RuntimeException {

    private final ErrorCode errorCode;

    /**
     * Creates an exception with the error code's safe default message.
     *
     * @param errorCode application error code describing the failure
     */
    public ApplicationException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }
}
