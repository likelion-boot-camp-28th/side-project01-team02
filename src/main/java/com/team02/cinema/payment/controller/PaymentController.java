package com.team02.cinema.payment.controller;

import com.team02.cinema.movie.domain.schedule.Schedule;
import com.team02.cinema.payment.repository.Payment;
import com.team02.cinema.payment.repository.PaymentType;
import com.team02.cinema.payment.service.PaymentService;
import com.team02.cinema.reservation.domain.Reservation;
import com.team02.cinema.reservation.domain.ReservationStatus;
import com.team02.cinema.theater.domain.Seat;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class PaymentController {

    private static final DateTimeFormatter SHOW_TIME_FORMAT =
            DateTimeFormatter.ofPattern("HH:mm");

    /// 영수증과 결제 내역에서 같은 날짜 형식을 사용한다.
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final PaymentService paymentService;
    private final Scanner sc;
    private final CouponController couponController;
    private final PointController pointController;

    public PaymentController(PaymentService paymentService, CouponController couponController, PointController pointController) {
        this.paymentService = paymentService;
        this.couponController = couponController;
        this.pointController = pointController;
        sc = new Scanner(System.in);
    }

    public PaymentService getPaymentService() {
        return paymentService;
    }

    public int readNonNegativeInt(String prompt) {

        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().strip();
            if (input.isEmpty()) {
                System.out.println("없으면 0을 입력해주세요.");
                continue;
            }
            try {
                int value = Integer.parseInt(input);
                if (value >= 0) {
                    return value;
                }
            } catch (NumberFormatException e) {

            }
            System.out.println("0 이상의 정수를 입력해 주세요.");
        }
    }

    public PaymentType readPaymentType() {
        PaymentType[] paymentTypes = PaymentType.values();
        System.out.println("\n===== 결제 수단 선택 =====");
        for (int i = 0; i < paymentTypes.length; i++) {
            // 메뉴 번호는 1부터, 배열 인덱스는 0부터 시작한다.
            System.out.printf("%d. %s%n", i + 1, paymentTypes[i].getMethodName());
        }

        while (true) {
            int choice = readNonNegativeInt("결제 선택: ");
            if (choice >= 1 && choice <= paymentTypes.length) {
                return paymentTypes[choice - 1];
            }
            System.out.println("메뉴에 있는 번호를 입력해 주세요.");
        }
    }

    private PaymentType.BankType readBankType() {
        PaymentType paymentType = readPaymentType();

        PaymentType.BankType[] bankTypes = PaymentType.BankType.values();
        System.out.println("\n===== 이체 은행 선택 =====");
        for (int i = 0; i < bankTypes.length; i++) {
            System.out.printf("%d. %s%n", i + 1, bankTypes[i].getBankName());
        }
        while (true) {
            int choice = readNonNegativeInt("은행 선택: ");
            if (choice >= 1 && choice <= bankTypes.length) {

                PaymentType.BankType bankType = bankTypes[choice - 1];
                bankType.setPayment(paymentType);
                return bankType;
            }
            System.out.println("메뉴에 있는 번호를 입력해 주세요.");
        }
    }


    public void paymentMenu(long memberId, int originalAmount, Reservation reservation) {

        // 결제 대기(PENDING) 상태의 예약만 결제할 수 있다. (이미 결제했거나 취소된 예약은 다시 결제하지 않도록)
        if (reservation.getStatus() != ReservationStatus.PENDING) {
            System.out.println("결제 대기 상태의 예약만 결제할 수 있습니다.");
            return;
        }

        System.out.println("\n===== 영화 결제 =====");
        printReservationDetails(reservation);
        System.out.println();
        System.out.printf("티켓 총금액: %d원%n", originalAmount);


        couponController.showAvailableCoupon(memberId);
        pointController.currentPoints(memberId);


        int couponId = readUseCoupon(memberId);
        int usePoint = readUsePoint(memberId);


        /// 결제 수단
        PaymentType.BankType bankType = readBankType();

        try {
            Payment payment = paymentService.pay(memberId, originalAmount, couponId, usePoint, bankType, reservation);
            // 결제가 성공하면 예약을 확정(CONFIRMED)한다.
            reservation.confirm();
            printReceipt(payment);

        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("결제 실패: " + e.getMessage());
            // 결제에 실패하면 예약을 취소해서 잡아둔 좌석을 다시 예매할 수 있게 한다.
            reservation.cancel();
            System.out.println("예약이 취소되어 좌석이 반환되었습니다. 다시 예매해 주세요.");
        }
    }

    private void printReceipt(Payment payment) {
        System.out.println("\n===== 결제 영수증 =====");
        System.out.println("결제번호: " + payment.getPaymentId());

        System.out.printf("티켓 금액: %,d원%n", payment.getOriginalAmount());
        System.out.printf("쿠폰 할인: %,d원%n", payment.getCouponDiscount());
        System.out.printf("포인트 사용: %,dP%n", payment.getUsedPoints());
        System.out.printf("최종 결제: %,d원%n", payment.getFinalAmount());
        System.out.println("결제 수단: " + payment.getPaymentMethod().getPaymentType().getMethodName());
        System.out.println("결제 일시: " + payment.getPaidAt().format(DATE_FORMAT));
        System.out.printf("포인트 적립: %,dP%n", payment.getEarnedPoints());
        System.out.printf("결제 직후 포인트: %,dP%n", payment.getRemainingPoints());
    }

    public void showMyPayments(long memberId) {
        List<Payment> payments = paymentService.getMyPayments(memberId);
        System.out.println("\n===== 내 결제 내역 =====");
        if (payments.isEmpty()) {
            System.out.println("결제 내역이 없습니다.");
            return;
        }
        payments.stream().forEach(payment -> {
            System.out.printf("%s | %,d원 | %s | %s%n",
                    payment.getPaymentId(),
                    payment.getFinalAmount(),
                    payment.getPaidAt().format(DATE_FORMAT),
                    payment.getStatus());

        });
    }

    private void printReservationDetails(Reservation reservation) {
        if (reservation == null) {
            System.out.println("  연결된 예매 정보가 없습니다.");
            return;
        }

        Schedule schedule = reservation.getSchedule();
        String seatNames = reservation.getSeats().stream()
                .map(Seat::getName)
                .collect(Collectors.joining(", "));

        System.out.printf("  예매번호: %s%n", reservation.getId());
        System.out.printf("  영화 제목: %s%n", schedule.getMovie().getTitle());
        System.out.printf("  상영 날짜: %s%n", schedule.getStartTime().toLocalDate());
        System.out.printf("  상영 시간: %s%n", schedule.getStartTime().format(SHOW_TIME_FORMAT));
        System.out.printf("  상영관: %d관%n", schedule.getScreen().getNum());
        System.out.printf("  선택 좌석: %s%n", seatNames);
        System.out.printf("  예매 인원: %d명%n", reservation.getSeats().size());
    }

    /// 환불
    public void refundMenu(long memberId) {
        System.out.println("\n===== 결제 환불 =====");
        System.out.print("환불할 결제번호 (뒤로 가기: Enter): ");
        String paymentId = sc.nextLine().strip();
        if (paymentId.isEmpty()) {
            return;
        }
        try {
            Payment payment = paymentService.refund(memberId, paymentId);
            System.out.printf("%s | 결제번호: %s | 환불 금액: %,d원%n",
                    payment.getStatus(),
                    payment.getPaymentId(),
                    payment.getFinalAmount());
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("환불 실패: " + e.getMessage());
        }
    }

    private int readUsePoint(long memberId) {
        while (true) {
            int points = readNonNegativeInt("포인트 입력 (미사용: 0): ");

            try {
                return pointController.usePoint(memberId, points);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private int readUseCoupon(long memberId) {
        while (true) {
            int coupon = readNonNegativeInt("쿠폰 입력 (미사용: 0): ");

            try {
                return couponController.selectCoupon(memberId, coupon);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
