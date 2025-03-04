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
    NOT_FOUND_DEFAULT_ADDRESS("기본 배송지를 설정해주세요.", HttpStatus.NOT_FOUND),

    UNAUTHORIZED_STORE_OWNER("가게 주인이 아닙니다.", HttpStatus.FORBIDDEN),

    STORE_STATUS_ALREADY_SAME("이미 해당 상태와 같습니다.", HttpStatus.BAD_REQUEST),

    Unauthorized_User("해당 권한이 없습니다", HttpStatus.UNAUTHORIZED),

    // 주문 에러
    LESS_THAN_MIN_ORDER_PRICE("최소 주문 금액 이상으로 주문 가능합니다.", HttpStatus.BAD_REQUEST),

    NOT_ENOUGH_POINT("보유 포인트가 부족합니다.", HttpStatus.BAD_REQUEST),
    NOT_OPENED_STORE("가게 오픈 이후에 주문 가능합니다.", HttpStatus.BAD_REQUEST)
    ;
    private final String message;

    private final HttpStatus httpStatus;
}
