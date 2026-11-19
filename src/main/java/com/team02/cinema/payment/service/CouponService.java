package com.team02.cinema.payment.service;

import com.team02.cinema.payment.repository.Coupon;

import java.util.LinkedHashMap;

import java.util.Map;

public class CouponService {
    /// // 쿠폰 발급, 조회, 사용


    /// Key: 쿠폰번호 / Value: 쿠폰 객체
    private final Map<Integer, Coupon> coupons = new LinkedHashMap<>(); /// 소유 쿠폰


    /// 회원에게 쿠폰 발급
    public void issueCoupon(Coupon coupon) {
        if (coupons.containsKey(coupon.getCouponId())) {    /// 중복 체크
            throw new IllegalArgumentException("중복 쿠폰 번호 입니다.");
        }
        coupons.put(coupon.getCouponId(), coupon);  /// 키 값 == 쿠폰 번호 일치 시킨다.

    }

    /// 회원이 가진 쿠폰 조회
    public void showMyCoupons(int memberId) {
        System.out.println("\n===== 내 쿠폰 조회 =====");

        int count = 0;

        for (Coupon value : coupons.values()) { /// 해당 회원 번호 쿠폰 조회
            if (value.getMemberId() != memberId){

            }
        }


    }

    /// 사용할 쿠폰의 할인금액 계산
    public int calculateDiscount() {

        return 1;
    }

    /// 결제 성공 후 쿠폰 사용 확정
    public void useCoupon() {

    }

}
