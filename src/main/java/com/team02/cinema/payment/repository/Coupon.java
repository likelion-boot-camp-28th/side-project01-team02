package com.team02.cinema.payment.repository;

import java.time.LocalDateTime;
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
    private final CouponName name;

    // 쿠폰 할인금액
    // 예: 3000이면 3,000원 할인
    private final int discountAmount;

    // 쿠폰의 사용 만료 날짜
    // 예: 2026-12-31
    private final LocalDateTime expirationDate;

    // 쿠폰 사용 여부
    // false = 미사용, true = 사용 완료
    private boolean used;


    public Coupon(LocalDateTime expirationDate, int discountAmount, CouponName name, int memberId, int couponId) {
        this.expirationDate = expirationDate;
        this.discountAmount = discountAmount;
        this.name = name;
        this.memberId = memberId;
        this.couponId = couponId;

        if (discountAmount < 0) {
            throw new IllegalArgumentException("할인 금액 오류");  /// 할인 금액 - 값이 될 수 없다.
        }
    }


    public int getCouponId() {
        return couponId;
    }

    public int getMemberId() {
        return memberId;
    }

    public CouponName getName() {
        return name;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public boolean isUsed() {
        return used;
    }

    /// 상태여부 메서드
    public String getStatus() {
        if (used) return "사용 완료";
        if (LocalDateTime.now().isAfter(expirationDate)) return "기간 만료";
        return "사용 가능";

    }

    /// 처리 오류 시 쿠폰 사용가능 복구
    public void restore() {
        used = false;
    }

    /// 쿠폰 사용
    public void use() {
        if (!isAvailable()) {    /// 사용 가능 여부
            throw new IllegalArgumentException("사용 불가능한 쿠폰");
        }
        used = true; /// 결과 사용함.
    }

    /// 쿠폰 상태 여부 확인 ->> 이미 썻는지 + 만료기간
    public boolean isAvailable() {
        /// 현재 쿠폰 사용 상태(이미썻는지 여부) + 현재 시간 < 쿠폰 만료 시간  ->>  사용불가 : false
        return !used && !LocalDateTime.now().isAfter(expirationDate);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Coupon coupon = (Coupon) object;
        return couponId == coupon.couponId && memberId == coupon.memberId && discountAmount == coupon.discountAmount && used == coupon.used && name == coupon.name && Objects.equals(expirationDate, coupon.expirationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(couponId, memberId, name, discountAmount, expirationDate, used);
    }
}
