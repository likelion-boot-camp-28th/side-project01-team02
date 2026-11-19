package com.team02.cinema.common;

/**
 * 모든 에러 문구를 한 곳에서 관리한다. 출력 시 OutputView가 "[ERROR] " 접두어를 붙인다.
 * 각 담당자는 자기 도메인 영역에 항목을 추가한다.
 */
public enum ErrorMessage {

    // 공통
    INVALID_INPUT("입력 형식이 올바르지 않습니다."),
    INVALID_MENU("존재하지 않는 메뉴입니다."),

    // 회원 (A)

    // 영화·상영 (B)

    // 예매·좌석 (C)

    // 결제·할인 (D)
    ;

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
