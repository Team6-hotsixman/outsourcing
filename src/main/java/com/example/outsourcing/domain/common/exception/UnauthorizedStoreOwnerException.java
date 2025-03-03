package com.example.outsourcing.domain.common.exception;

public class UnauthorizedStoreOwnerException extends ApplicationException {
    public UnauthorizedStoreOwnerException() {
        super(ErrorCode.UNAUTHORIZED_STORE_OWNER);
    }
}
