package com.team02.cinema.common;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 모든 콘솔 출력은 이 클래스를 거친다.
 */
public class OutputView {

    private static final String ERROR_PREFIX = "[ERROR] ";
    private static final DecimalFormat MONEY_FORMAT = new DecimalFormat("#,###");
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public void printLine(String message) {
        System.out.println(message);
    }

    public void printError(ErrorMessage errorMessage) {
        System.out.println(ERROR_PREFIX + errorMessage.getMessage());
    }

    public static String formatMoney(long amount) {
        return MONEY_FORMAT.format(amount) + "원";
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME_FORMAT);
    }
}
