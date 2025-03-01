package com.example.outsourcing.domain.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private String message;

    public ErrorCode errorCode;

    private Map<String, String> fieldError;
}
