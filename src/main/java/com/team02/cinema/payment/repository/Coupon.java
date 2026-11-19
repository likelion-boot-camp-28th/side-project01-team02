package com.team02.cinema.payment.repository;

import java.time.LocalDate;
import java.util.Objects;

public class Coupon {

    //[ 내 쿠폰 조회 ]
    //
    //1. 신규회원 할인쿠폰
    //   할인금액 : 3,000원
    //   유효기간 : 2026-12-31
    //   상태     : 사용 가능
    //
    //2. 주말 영화 할인쿠폰
    //   할인금액 : 2,000원
    //   유효기간 : 2026-11-30
    //   상태     : 사용 가능


    // 쿠폰마다 부여되는 고유번호
    // 예: 1001, 1002
    private final int couponId;

    // 쿠폰을 소유한 회원의 고유번호
    // 예: 회원번호 1번이 보유한 쿠폰
    private final int memberId;

    // 쿠폰 이름
    // 예: "신규회원 할인쿠폰"
    private final String name;

    // 쿠폰 할인금액
    // 예: 3000이면 3,000원 할인
    private final int discountAmount;

    // 쿠폰의 사용 만료 날짜
    // 예: 2026-12-31
    private final LocalDate expirationDate;

    // 쿠폰 사용 여부
    // false = 미사용, true = 사용 완료
    private boolean used;

    public Coupon(int couponId, int memberId, String name, int discountAmount, LocalDate expirationDate) {
        if (discountAmount < 0) {
            throw new IllegalArgumentException("할인금액은 음수일 수 없습니다.");
        }
        this.couponId = couponId;
        this.memberId = memberId;
        this.name = name;
        this.discountAmount = discountAmount;
        this.expirationDate = Objects.requireNonNull(expirationDate);
    }

    public int getCouponId() { return couponId; }
    public int getMemberId() { return memberId; }
    public String getName() { return name; }
    public int getDiscountAmount() { return discountAmount; }
    public LocalDate getExpirationDate() { return expirationDate; }
    public boolean isUsed() { return used; }

    
}
