package com.team02.cinema.payment.domain;


import com.team02.cinema.payment.controller.CouponController;
import com.team02.cinema.payment.controller.PaymentController;
import com.team02.cinema.payment.service.CouponService;
import com.team02.cinema.payment.service.PaymentService;

import java.util.Scanner;

public class PaymentMenu {

    private static final int DEMO_MEMBER_ID = 1;

    public static void main(String[] args) {
        CouponController coupons = new CouponController(new CouponService());
        PaymentController points = new PaymentController();

        Scanner sc = new Scanner(System.in);

        int inputNumber = 0;


        while (true) {
            System.out.println("""
                    ========================================
                               CINEMA PAYMENT
                    ========================================
                      [1] 영화 예매 결제
                      [2] 결제 내역 조회
                      [3] 결제 취소 / 환불
                      [4] 내 쿠폰 조회
                      [5] 내 포인트 조회
                      [6] 영화 리뷰 관리
                      [0] 이전 메뉴
                    ========================================
                    """);
            inputNumber = sc.nextInt();
            sc.nextLine();


            switch (inputNumber) {

                case 1:

                break;
                case 2:

                break;
                case 3:

                break;
                case 4:

                break;
                case 5:

                break;
                case 6:

                break;
                case 0:

                break;

                default:
                    System.out.println("");
            }
        }
    }
}
