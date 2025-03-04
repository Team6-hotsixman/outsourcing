package com.example.outsourcing.domain.common.exception;

public class UnauthorizedUserException extends ApplicationException {
    public UnauthorizedUserException() {
        super(ErrorCode.Unauthorized_User);
    }
}
