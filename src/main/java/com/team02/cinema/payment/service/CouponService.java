package com.team02.cinema.payment.service;

import com.team02.cinema.payment.repository.Coupon;

import java.util.LinkedHashMap;

import java.util.List;
import java.util.Map;

public class CouponService {
    /// // 쿠폰 발급, 조회, 사용

    /// 일단: // 모든 회원의 쿠폰 목록
    /// Key: 쿠폰번호 / Value: 쿠폰 객체
    private final Map<Integer, Coupon> coupons = new LinkedHashMap<>(); /// 소유 쿠폰


    /// 회원에게 쿠폰 발급
    public void issueCoupon(Coupon coupon) {
        if (coupons.containsKey(coupon.getCouponId())) {    /// 중복 체크
            throw new IllegalArgumentException("중복 쿠폰 번호 입니다.");
        }
        coupons.put(coupon.getCouponId(), coupon);

    }

    /// 회원이 가진 쿠폰 조회
    public List<Coupon> getMyCoupons(int memberId) {

        return coupons.values().stream()
                .filter(coupon -> coupon.getMemberId() == memberId)
                .toList();
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
