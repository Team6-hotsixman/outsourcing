package com.example.outsourcing.domain.common.exception;

public class StoreLimitExceededException extends ApplicationException {
    public StoreLimitExceededException() {
        super(ErrorCode.STORE_LIMIT_EXCEEDED);
    }
}
