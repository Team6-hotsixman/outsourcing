package com.example.outsourcing.domain.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    NOT_FOUND_USER("없는 사용자 입니다.", HttpStatus.NOT_FOUND),
    //카테고리 에러
    NOT_FOUND_CATEGORY("없는 카테고리 입니다.", HttpStatus.NOT_FOUND),
    DUPLICATE_CATEGORY("이미 존재하는 카테고리입니다.", HttpStatus.BAD_REQUEST),

    STORE_LIMIT_EXCEEDED("한 사장님은 최대 3개까지만 가게를 생성할 수 있습니다.", HttpStatus.BAD_REQUEST),

    METHOD_ARGUMENT_NOT_VALID("입력값이 올바르지 않습니다", HttpStatus.BAD_REQUEST),
    ;

    private final String message;

    private final HttpStatus httpStatus;
}
