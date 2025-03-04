package com.example.outsourcing.domain.common.exception;

public class NotFoundStoreException extends ApplicationException{
    public NotFoundStoreException() {
        super(ErrorCode.NOT_FOUND_STORE);
    }

}
