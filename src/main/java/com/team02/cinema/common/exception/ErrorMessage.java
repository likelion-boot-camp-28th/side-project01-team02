package com.team02.cinema.common.exception;

public enum ErrorMessage {
    DUPLICATE_LOGIN_ID("이미 존재하는 아이디입니다."),
    LOGIN_FAILED("아이디 또는 비밀번호가 일치하지 않습니다.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage(){
        return "[ERROR] " + message;
    }
}
