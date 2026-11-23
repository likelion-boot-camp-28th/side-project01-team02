package com.team02.cinema.payment.domain;


import com.team02.cinema.member.domain.Member;
import com.team02.cinema.payment.controller.CouponController;
import com.team02.cinema.payment.controller.PaymentController;
import com.team02.cinema.payment.controller.PointController;
import com.team02.cinema.payment.service.CouponService;
import com.team02.cinema.payment.service.PaymentService;
import com.team02.cinema.payment.service.PointService;
import com.team02.cinema.reservation.domain.Reservation;

import java.awt.*;
import java.util.Scanner;

public class PaymentMenu {

    private static final int DEMO_TICKET_AMOUNT = 24_000;
    private final PaymentController paymentController;
    private final CouponController couponController;
    private final PointController pointController;
    private Member member;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }


    public PaymentMenu(PaymentController paymentController, CouponController couponController, PointController pointController) {
        this.paymentController = paymentController;
        this.couponController = couponController;
        this.pointController = pointController;
    }

    public void runMemberId(Member member, Scanner sc, Reservation reservation) {
        run(member.getId(), sc, reservation);
    }

    public void run(long memberId, Scanner sc, Reservation reservation) {

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
                      [0] 이전 메뉴
                    ========================================
                    """);
            System.out.print("메뉴 선택: ");
            /// 입력이 끝났을 때 nextLine()의 NoSuchElementException을 방지한다.
            if (!sc.hasNextLine()) {
                System.out.println("\n입력이 종료되어 결제 메뉴를 닫습니다.");
                return;
            }
            String choice = sc.nextLine().strip();

            switch (choice) {
                case "1" -> {
                    try {
                        if (reservation == null) {
                            System.out.println("결제할 예매를 먼저 선택해 주세요.");
                            break;
                        }
                        int pricePerSeat = reservation.getSchedule().getBasePrice();
                        int seatCount = reservation.getSeats().size();
                        int originalAmount = pricePerSeat * seatCount;


                        paymentController.paymentMenu(memberId, originalAmount, reservation);

                    } catch (IllegalArgumentException | IllegalStateException e) {
                        System.out.println("결제 실패: " + e.getMessage());
                    }
                }
                case "2" -> {
                    try {

                        paymentController.showMyPayments(memberId);

                    } catch (IllegalArgumentException | IllegalStateException e) {
                        System.out.println("결제 실패: " + e.getMessage());
                    }
                }
                case "3" -> {

                    try {

                        paymentController.refundMenu(memberId);

                    } catch (IllegalArgumentException | IllegalStateException e) {
                        System.out.println("결제 실패: " + e.getMessage());
                    }
                }
                case "4" -> {
                    try {
                        select:
                        while (true) {
                            System.out.println("""
                                    ========================================
                                               CINEMA PAYMENT
                                    ========================================
                                      [1] 쿠폰 보유 개수
                                      [2] 사용 가능한 쿠폰 개수
                                      [3] 내 쿠폰 전체 조회
                                      [0] 이전 메뉴
                                    ========================================
                                    """);
                            System.out.print("메뉴 입력: ");
                            String input = sc.nextLine().strip();
                            if (input.isEmpty()) {
                                System.out.println("다시 입력해주세요. ");
                                continue;
                            }
                            try {
                                int value = Integer.parseInt(input);
                                if (value >= 0) {
                                    switch (value) {
                                        case 1 -> couponController.showCounponCount(memberId);
                                        case 2 -> couponController.showAvailableCoupon(memberId);
                                        case 3 -> couponController.showMyCoupons(memberId);
                                        case 0 -> {break select;}
                                        default -> System.out.println("1,2,3,0 정수를 입력해 주세요.");
                                    }
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("1,2,3,0 정수를 입력해 주세요.");
                            }
                        }

                    } catch (IllegalArgumentException | IllegalStateException e) {
                        System.out.println("결제 실패: " + e.getMessage());
                    }
                }
                case "5" -> {
                    try {
                        select:
                        while (true) {
                            System.out.println("""
                                    ========================================
                                               CINEMA PAYMENT
                                    ========================================
                                      [1] 포인트 사용 내역
                                      [2] 포인트 적립 내역
                                      [3] 결제의 포인트 내역
                                      [4] 포인트 전체 내역
                                      [0] 이전 메뉴
                                    ========================================
                                    """);
                            System.out.print("메뉴 입력: ");
                            String input = sc.nextLine().strip();
                            if (input.isEmpty()) {
                                System.out.println("다시 입력해주세요.");
                                continue;
                            }
                            try {
                                int value = Integer.parseInt(input);
                                if (value >= 0) {
                                    switch (value) {
                                        case 1 -> pointController.showUsePoints(memberId);
                                        case 2 -> pointController.showEarnedPointHistory(memberId);
                                        case 3 -> {
                                            System.out.print("결제 번호: ");
                                            String paymentId = sc.nextLine().strip();
                                            pointController.showPaymentPointHistory(memberId, paymentId);
                                        }
                                        case 4 -> pointController.showPointHistory(memberId);
                                        case 0 -> {
                                            break select;
                                        }
                                        default -> System.out.println("1,2,3,0 정수를 입력해 주세요.");
                                    }
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("1,2,3,0 정수를 입력해 주세요.");
                            }
                        }
                    } catch (IllegalArgumentException | IllegalStateException e) {
                        System.out.println("결제 실패: " + e.getMessage());
                    }
                }
                case "0" -> {
                    return;
                }
                default -> System.out.println("0부터 5까지의 메뉴 번호를 입력해 주세요.");
            }
        }
    }
}

