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

        int count = 0; /// 쿠폰 갯수

        for (Coupon value : coupons.values()) { /// 해당 회원 번호 쿠폰 조회
            if (value.getMemberId() != memberId) {
                continue;       /// 없는 경우 출력(X)
            }

            /// 있는 경우
            count++;    /// 증가
            System.out.println(value.getName());
            System.out.println("  할인금액: " + value.getDiscountAmount());
            System.out.println("  유효기간: " + value.getExpirationDate());
            System.out.println("  상태: " + value.getStatus());
            System.out.println("  쿠폰번호: " + value.getCouponId());
        }


        if (count == 0) System.out.println("보유한 쿠폰이 없습니다.");

    }

    /// 사용할 쿠폰의 할인금액 계산
    /**
     * @param memberId     회원 고유번호
     * @param coupoonId    쿠폰 번호
     * @param ticketAmount 티켓 원가
     * @return
     */
    public int calculateDiscount(int memberId, int coupoonId, int ticketAmount) {

        Coupon coupon = findCoupon(memberId, coupoonId);

        if (!coupon.isAvailable()) {
            System.out.println("사용 할 수 없는 쿠폰입니다. ");
        }

        /// 할인금액이 티켓 금액을 넘지 않도록 제한 : - 금액 방지
        return Math.min(coupon.getDiscountAmount(), ticketAmount);

    }

    /// 해당 쿠폰 ->> 쿠폰 조회 및 소유자 검사
    private Coupon findCoupon(int memberId, int couponId) {

        Coupon coupon = coupons.get(couponId);  ///  쿠폰 번호 ->> 쿠폰 존재 여부
        if (coupon == null || coupon.getMemberId() != memberId) {  /// 해당 쿠폰 보유 id == 회원 id 체크
            System.out.println("쿠폰을 찾을 수 없습니다.");

        }

        return coupon;  /// 있으면 해당 값 반환
    }


    /// 결제 성공 시 쿠폰 사용
    public void useCoupon(int memberId, int couponId) {

        Coupon coupon = findCoupon(memberId, couponId);
        coupon.use();
    }



}
