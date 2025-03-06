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
    NOT_FOUND_REVIEW("없는 리뷰 입니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_ORDER("없는 주문 입니다.", HttpStatus.NOT_FOUND),


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

    AUTH_EXCEPTION("@AUTH와 AUTHUSER는 같이 사용되어야 합니다.",HttpStatus.INTERNAL_SERVER_ERROR),

    INVALID_REFRESH_TOKEN("Refresh Token이 올바르지 않거나 만료되었습니다.",HttpStatus.BAD_REQUEST),

    // 주문 에러
    LESS_THAN_MIN_ORDER_PRICE("최소 주문 금액 이상으로 주문 가능합니다.", HttpStatus.BAD_REQUEST),

    NOT_ENOUGH_POINT("보유 포인트가 부족합니다.", HttpStatus.BAD_REQUEST),

    NOT_OPENED_STORE("가게 오픈 이후에 주문 가능합니다.", HttpStatus.BAD_REQUEST),

    MISMATCHED_ORDER_WITH_STORE("해당 가게에서 주문하지 않았습니다.", HttpStatus.BAD_REQUEST),

    MISMATCHED_ORDER_WITH_USER("주문을 요청한 고객이 아닙니다.", HttpStatus.UNAUTHORIZED),

    ORDER_STATUS_ALREADY_SAME("이미 해당 상태와 같습니다.", HttpStatus.BAD_REQUEST),

    CANT_CANCEL_AFTER_COOKING("주문 수락 이후에는 취소할 수 없습니다. 가게로 연락하세요", HttpStatus.NOT_ACCEPTABLE),

    // 통계
    INVALID_DATE_FORMAT("유효하지 않은 날짜 형식입니다.", HttpStatus.BAD_REQUEST),

    // 유저 주소 에러
    INVALID_ADDRESS("잘못된 주소 입니다.", HttpStatus.BAD_REQUEST),
    DUPLICATE_ADDRESS("이미 사용중인 주소 입니다.", HttpStatus.BAD_REQUEST),
    NOT_FOUND_DEFAULT_ADDRESS("기본 주소를 설정해주세요.", HttpStatus.NOT_FOUND),
    NOT_FOUND_ADDRESS("없는 주소 입니다.", HttpStatus.NOT_FOUND),
    ;

    private final String message;

    private final HttpStatus httpStatus;
}
