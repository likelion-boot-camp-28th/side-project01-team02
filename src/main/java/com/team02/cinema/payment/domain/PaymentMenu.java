package com.team02.cinema.payment.domain;

import com.team02.cinema.payment.repository.Payment;
import com.team02.cinema.payment.repository.Coupon;
import com.team02.cinema.payment.service.CouponService;
import com.team02.cinema.payment.service.PaymentService;
import com.team02.cinema.payment.service.PointService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class PaymentMenu {

    private static final int DEMO_MEMBER_ID = 1;

    public static void main(String[] args) {
        CouponService coupons = new CouponService();
        PointService points = new PointService();

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
