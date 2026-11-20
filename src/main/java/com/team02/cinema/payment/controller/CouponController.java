package com.team02.cinema.payment.controller;

import com.team02.cinema.payment.repository.Coupon;
import com.team02.cinema.payment.service.CouponService;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class CouponController {

    private final CouponService couponService;
    private final Scanner scanner;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
        this.scanner = new Scanner(System.in);
    }

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public void showMyCoupons(int memberId) {

        List<Coupon> myCoupons = couponService.getMyCoupons(memberId);


        System.out.println("\n===== 내 쿠폰 조회 =====");


        if (myCoupons.isEmpty())
            System.out.println("보유한 쿠폰이 없습니다.");
        else {
            myCoupons.forEach(coupon -> {
                System.out.println("  쿠폰 보유: " + myCoupons.size());
                System.out.println("  쿠폰 이름: " + coupon.getName());
                System.out.println("  할인금액: " + coupon.getDiscountAmount());
                System.out.println("  유효기간: " + coupon.getExpirationDate());
                System.out.println("  상태: " + coupon.getStatus());
                System.out.println("  쿠폰번호: " + coupon.getCouponId());

            });
        }
        System.out.println("\n======================");

    }

    public void showAvailableCoupon(int memberId) {

        List<Coupon> availableCounpons = couponService.getAvailableCoupons(memberId);

        System.out.println("\n===== 사용 가능한 쿠폰 =====");
        if (availableCounpons.isEmpty()) {
            System.out.println("사용 가능한 쿠폰이 없습니다.");
            return;
        }
        availableCounpons.forEach(coupon -> {
                    System.out.println("  쿠폰 보유: " + availableCounpons.size());
                    System.out.println("  쿠폰 이름: " + coupon.getName());
                    System.out.println("  할인금액: " + coupon.getDiscountAmount());
                    System.out.println("  유효기간: " + coupon.getExpirationDate());
                    System.out.println("  상태: " + coupon.getStatus());
                    System.out.println("  쿠폰번호: " + coupon.getCouponId());
                }
        );
    }

    /// 결제에 사용할 쿠폰 선택
    public int selectCoupon(int memberId) {

        showAvailableCoupon(memberId);

        System.out.println("사용할 쿠폰번호 입력: ");

        int couponId = Integer.parseInt(scanner.nextLine());

        if (couponId == 0) {
            return 0;
        }

        Coupon coupon = couponService.findCoupon(memberId, couponId);

        if (!coupon.isAvailable())
            throw new IllegalArgumentException("사용 할 수 없는 쿠폰 입니다.");

        return couponId;
    }

    /// 쿠폰 보유 개수 출력
    public void showCounponCount(int memberId) {

        int total = couponService.getMyCoupons(memberId).size();
        long available = couponService.countAvailableCoupon(memberId);


        System.out.println("\n===== 쿠폰 보유 현황 =====");
        System.out.println("전체 쿠폰 : " + total + "개");
        System.out.println("사용 가능 : " + available + "개");
        System.out.println("==========================");

    }


}


