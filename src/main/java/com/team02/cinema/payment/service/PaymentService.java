package com.team02.cinema.payment.service;

import com.team02.cinema.payment.repository.Payment;
import com.team02.cinema.payment.repository.PaymentType;
import com.team02.cinema.reservation.domain.Reservation;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PaymentService {


    /// 일단: 모든 결제 내용
    ///   결제가 완료된 Payment 객체들을 저장
    ///
    ///   Key   : 결제번호
    ///   Value : 결제 객체
    Map<String, Payment> payments = new LinkedHashMap<>();

    /// 결제번호 -> 회원 번호
    private final Map<String, Long> paymentOwners = new HashMap<>();


    /// // 결제 처리 및 영수증
    private final CouponService couponService;
    private final PointService pointService;

    public PaymentService(CouponService couponService, PointService pointService) {
        this.couponService = couponService;
        this.pointService = pointService;
    }


    /// 결제번호 생성에 사용하는 일련번호
    /// 결제가 생성될 때마다 1씩 증가
    private static int nextPaymentNumber = 1;


    ///  결제 처리
    public Payment pay(long memberId, int originalAmount, Integer couponId, int usePoint, PaymentType.BankType paymentType, Reservation reservation) {


        /// 결제 금액 검증
        if (originalAmount <= 0) {
            System.out.println("결제 금액은 0원 보다 커야 됩니다. (음수가 될 수없습니다)");
        }

        if (usePoint < 0) {
            System.out.println("사용 포인트는 음수 일 수 없습니다.");
        }

        if (paymentType.getPaymentType() == null) {
            System.out.println("결제 수단을 선택 해주세요. ");
        }
        /// 쿠폰 미사용 null,
        int selectCouponId = (couponId == null) ? 0 : couponId;

        /// 쿠폰 할인 금액 측정
        int couponDiscount = couponService.calculateDiscount(memberId, couponId, originalAmount);

        int resultAmount = originalAmount - couponDiscount;

        /// 포인트 사용
        if (usePoint > resultAmount) {
            System.out.println("결제 금액 보다 많은 포인트를 사용할 수 업습니다.");
        }

        if (pointService.getBalance(memberId) < usePoint) {
            System.out.println("보유 포인트가 부족합니다.");
        }
        int finalAmount = resultAmount - usePoint;

        int earnedPoints = finalAmount * 5 / 100;     /// 적립 포인트 게산 (%5)


        /// 결제 번호 생성
        String paymentId = String.format("P%s%03d", LocalDate.now()
                .format(DateTimeFormatter.BASIC_ISO_DATE), nextPaymentNumber);


        /// 쿠폰 사용
        couponService.useCoupon(memberId, selectCouponId);

        /// 포인트 사용
        pointService.usePoint(memberId, usePoint, paymentId);

        /// 포인트 적립
        pointService.earnPoint(memberId, earnedPoints, paymentId);

        /// 포인트 잔액
        int remainingPoint = pointService.getBalance(memberId);


        Payment payment = new Payment(paymentId, selectCouponId, originalAmount,
                couponDiscount, usePoint, finalAmount, earnedPoints, remainingPoint, paymentType, reservation);


        payments.put(paymentId, payment);
        paymentOwners.put(paymentId, memberId);

        nextPaymentNumber++;

        return payment;
    }

    public Payment pay(long memberId, int originalAmount, int usePoint, PaymentType.BankType paymentType, Reservation reservation) {
        return pay(memberId, originalAmount, null, usePoint, paymentType, reservation);

    }


    /// 결제번호로 조회
    public Payment findPayment(long memberId, String paymentId) {

        Payment payment = payments.get(paymentId);

        if (payment == null) {
            throw new IllegalArgumentException("해당 결제 내역을 찾을 수 없습니다.");
        }

        return payment;
    }

    /// 회원별 전체 결제 내역 조회
    public List<Payment> getMyPayments(long memberId) {
        return payments.values().stream()
                .filter(payment -> paymentOwners.get(payment.getPaymentId()) == memberId)
                .sorted(Comparator.comparing(Payment::getPaidAt).reversed()).toList();

    }

    /// 결제 완료 내역 조회
    public List<Payment> getCompletedPayment(long memberId) {

        return getMyPayments(memberId).stream()
                .filter(payment -> !payment.isRefunded()).toList();
    }

    /// 환불 완료 내역 조회
    public List<Payment> getRefundedPayments(long memberId) {

        return getMyPayments(memberId).stream()
                .filter(payment -> payment.isRefunded()).toList();
    }

    /// 총 결제 금액
    public int getTotalPaidAmount(long memberId) {

        return getCompletedPayment(memberId).stream()
                .mapToInt(payment -> payment.getFinalAmount()).sum();


    }

    /// 환불 처리
    public Payment refund(long memberId, String paymentId) {

        Payment payment = findPayment(memberId, paymentId);

        if (payment.isRefunded()) { /// 환불 검사
            throw new IllegalArgumentException("이미 환불된 결제 입니다.");
        }

        int currentBalance = pointService.getBalance(memberId);
        int expectedBalance = currentBalance + payment.getUsedPoints();
        int earnedPoints = payment.getEarnedPoints();


        if (expectedBalance < earnedPoints) {

            throw new IllegalStateException("적립 포인트를 회수하기에 잔액이 부족하여 환불할 수 없습니다.");
        }
        if (payment.getUsedPoints() > 0)
            pointService.restorePoint(memberId, paymentId);
        if (payment.getEarnedPoints() > 0)
            pointService.revokePoints(memberId, paymentId);


        /// 쿠폰 복구
        if (payment.getCouponId() != null) {
            couponService.restoreCoupon(memberId, payment.getCouponId());
        }

        payment.refund();

        return payment;
    }
}