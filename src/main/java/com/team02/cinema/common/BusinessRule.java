package com.team02.cinema.common;

/**
 * 요구사항 명세서 4장의 숫자를 한 곳에서 관리한다. (초안 값 - 팀 확정 후 수정)
 */
public final class BusinessRule {

    // 좌석과 예매
    public static final int MAX_SEATS_PER_RESERVATION = 4;
    public static final int ADULT_AGE = 19;

    // 취소와 환불
    public static final int CANCEL_DEADLINE_MINUTES = 20;

    // 가격과 할인
    public static final int PREMIUM_SEAT_EXTRA_PRICE = 2_000;
    public static final int EARLY_BIRD_DISCOUNT_PER_SEAT = 2_000;
    public static final int EARLY_BIRD_HOUR_LIMIT = 11;
    public static final int YOUTH_DISCOUNT_PER_SEAT = 3_000;
    public static final double POINT_ACCUMULATION_RATE = 0.05;

    // 관리자
    public static final int CLEANING_MINUTES = 10;

    private BusinessRule() {
    }
}
