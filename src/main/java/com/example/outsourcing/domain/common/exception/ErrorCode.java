package com.example.outsourcing.domain.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // NOT FOUND EXCEPTION
    NOT_FOUND_USER("없는 사용자 입니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_STORE("없는 가게 입니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_CATEGORY("없는 카테고리 입니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_MENU("없는 메뉴 입니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_IMAGE("없는 이미지 입니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_MENU_OPTION("없는 메뉴 옵션 입니다.", HttpStatus.NOT_FOUND),

    //카테고리 에러
    DUPLICATE_CATEGORY("이미 존재하는 카테고리입니다.", HttpStatus.BAD_REQUEST),

    STORE_LIMIT_EXCEEDED("한 사장님은 최대 3개까지만 가게를 생성할 수 있습니다.", HttpStatus.BAD_REQUEST),

    METHOD_ARGUMENT_NOT_VALID("입력값이 올바르지 않습니다", HttpStatus.BAD_REQUEST),

    UNAUTHORIZED_STORE_OWNER("가게 주인이 아닙니다.", HttpStatus.FORBIDDEN),

    STORE_STATUS_ALREADY_SAME("이미 해당 상태와 같습니다.", HttpStatus.BAD_REQUEST),

    Unauthorized_User("해당 권한이 없습니다", HttpStatus.UNAUTHORIZED),


    //유저 에러
    DUPLICATE_EMAIL("이미존재하는 이메일입니다.", HttpStatus.BAD_REQUEST),

    PASSWORD_ARGUMENT_NOT_VALID("비밀번호가 틀렸습니다.",HttpStatus.BAD_REQUEST),

    DUPLICATE_PASSWORD("새 비밀번호는 기존 비밀번호와 같을 수 없습니다.",HttpStatus.BAD_REQUEST),

    MISS_MATCH_PASSWORD("기존 비밀번호와 일치하지 않습니다.",HttpStatus.BAD_REQUEST),

    USER_STATUS_DELETE("이미 탈퇴한 회원입니다.",HttpStatus.BAD_REQUEST),

    AUTH_EXCEPTION("@AUTH와 AUTHUSER는 같이 사용되어야 합니다.",HttpStatus.INTERNAL_SERVER_ERROR);

    ;
    private final String message;

    private final HttpStatus httpStatus;
}
