package com.team02.cinema.common.exception;

public enum ErrorMessage {
    DUPLICATE_LOGIN_ID("이미 존재하는 아이디입니다."),
    LOGIN_FAILED("아이디 또는 비밀번호가 일치하지 않습니다."),
    INVALID_BIRTH_DATE("생년월일 형식이 올바르지 않습니다. (예: 2000-01-15)"),
    INVALID_MENU("잘못 입력하셨습니다."),
    HAS_CONFIRMED_RESERVATION("확정된 예매가 있어 탈퇴할 수 없습니다.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage(){
        return "[ERROR] " + message;
    }
}
