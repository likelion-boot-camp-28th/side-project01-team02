package com.team02.cinema.payment.repository;

public enum CouponName {

    FREE_TICKET("1인 무료 관람권"),
    FIXED_3000("3,000원 할인 쿠폰"),
    PERCENT_20("20% 할인 쿠폰"),
    EARLY_BIRD("조조/심야 2000원 할인 쿠폰"),
    WELCOME_FLAT_3000("신규 가입 3,000원 즉시 할인");


    private String korName;

    CouponName(String korName) {
        this.korName = korName;
    }

    @Override
    public String toString() {
        return korName;
    }
}
