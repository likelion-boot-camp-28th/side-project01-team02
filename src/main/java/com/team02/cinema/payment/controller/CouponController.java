package com.team02.cinema.payment.controller;

import com.team02.cinema.payment.repository.Coupon;
import com.team02.cinema.payment.service.CouponService;

import java.util.List;
import java.util.Scanner;

public class CouponController {

    private final CouponService couponService;
    private final Scanner scanner;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
        this.scanner = new Scanner(System.in);
    }


    public void showMyCoupons(int memberId) {

        List<Coupon> myCoupons = couponService.getMyCoupons(memberId);


        System.out.println("\n===== 내 쿠폰 조회 =====");


        if (myCoupons.isEmpty())
            System.out.println("보유한 쿠폰이 없습니다.");
        else {
            myCoupons.forEach(coupon -> {
                System.out.println("쿠폰 보유: " + myCoupons.size());
                System.out.println(coupon.getName());
                System.out.println("  할인금액: " + coupon.getDiscountAmount());
                System.out.println("  유효기간: " + coupon.getExpirationDate());
                System.out.println("  상태: " + coupon.getStatus());
                System.out.println("  쿠폰번호: " + coupon.getCouponId());

            });
        }
        System.out.println("\n======================");

    }
}


