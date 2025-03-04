package com.example.outsourcing.config;

import com.example.outsourcing.domain.common.exception.ApplicationException;
import com.example.outsourcing.domain.common.exception.ErrorCode;
import com.example.outsourcing.domain.common.exception.ErrorResponse;
import com.example.outsourcing.domain.common.exception.StoreLimitExceededException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> exHandle(ApplicationException e) {
        return ResponseEntity.status(e.getStatus()).body(new ErrorResponse(e.getMessage(),e.getErrorCode(), null));
    }

    //@Valid 유효성 체크
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorCode errorCode = ErrorCode.METHOD_ARGUMENT_NOT_VALID;

        // 유효성 검사 실패한 모든 필드와 에러 메시지를 추출
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        Map<String, String> errorFields = fieldErrors.stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (m1, m2) -> m1));

        ErrorResponse response = new ErrorResponse(errorCode.getMessage(),errorCode, errorFields);

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }


}
