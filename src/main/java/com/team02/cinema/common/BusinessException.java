package com.team02.cinema.common;

/**
 * 비즈니스 규칙 위반 시 던지는 예외. View에서 잡아 에러 메시지를 출력한다.
 */
public class BusinessException extends RuntimeException {

    private final ErrorMessage errorMessage;

    public BusinessException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }
}
