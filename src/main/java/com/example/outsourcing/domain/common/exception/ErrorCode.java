package com.example.outsourcing.domain.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    NOT_FOUND_USER("없는 사용자 입니다.", HttpStatus.NOT_FOUND),

    STORE_LIMIT_EXCEEDED("한 사장님은 최대 3개까지만 가게를 생성할 수 있습니다.", HttpStatus.BAD_REQUEST),

    METHOD_ARGUMENT_NOT_VALID("입력값이 올바르지 않습니다", HttpStatus.BAD_REQUEST),

    NOT_FOUND_STORE("없는 가게 입니다.", HttpStatus.NOT_FOUND),

    UNAUTHORIZED_STORE_OWNER("가게 주인이 아닙니다.", HttpStatus.FORBIDDEN),

    STORE_STATUS_ALREADY_SAME("이미 해당 상태와 같습니다.", HttpStatus.BAD_REQUEST),
    ;

    private final String message;

    private final HttpStatus httpStatus;
}
