package com.example.outsourcing.domain.common.exception;

public class StoreStatusAlreadySameException extends ApplicationException {
    public StoreStatusAlreadySameException() {
        super(ErrorCode.STORE_STATUS_ALREADY_SAME);
    }
}
