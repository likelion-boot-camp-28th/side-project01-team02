package com.team02.cinema.payment.service;

import com.team02.cinema.member.domain.Member;
import com.team02.cinema.payment.repository.Payment;
import com.team02.cinema.payment.repository.PaymentType;
import com.team02.cinema.reservation.domain.Reservation;
import com.team02.cinema.reservation.service.ReservationService;


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
    private final ReservationService reservationService;


    public PaymentService(CouponService couponService, PointService pointService, ReservationService reservationService) {
        this.couponService = couponService;
        this.pointService = pointService;
        this.reservationService = reservationService;
    }


    /// 결제번호 생성에 사용하는 일련번호
    /// 결제가 생성될 때마다 1씩 증가
    private static int nextPaymentNumber = 1;


    ///  결제 처리
    public Payment pay(long memberId, int originalAmount, Integer couponId, int usePoint, PaymentType.BankType paymentType, Reservation reservation) {


        if (reservation == null) {
            throw new IllegalArgumentException("결제할 예매 정보가 필요합니다.");
        }
        if (originalAmount <= 0) {
            throw new IllegalArgumentException("결제 금액은 0원보다 커야 합니다.");
        }
        if (usePoint < 0) {
            throw new IllegalArgumentException("사용 포인트는 음수일 수 없습니다.");
        }
        if (paymentType == null || paymentType.getPaymentType() == null) {
            throw new IllegalArgumentException("결제 수단을 선택해 주세요.");
        }


        /// 쿠폰 미사용 null,
        int selectedCouponId = (couponId == null) ? 0 : couponId;

        /// 쿠폰 할인 금액 측정
        int couponDiscount = couponService.calculateDiscount(memberId, couponId, originalAmount);
        int resultAmount = originalAmount - couponDiscount;
        int balanceBeforePayment = pointService.getBalance(memberId);

        /// 포인트 사용
        if (usePoint > resultAmount) {
            System.out.println("결제 금액 보다 많은 포인트를 사용할 수 업습니다.");
        }

        if (pointService.getBalance(memberId) < usePoint) {
            System.out.println("보유 포인트가 부족합니다.");
        }

        int finalAmount = resultAmount - usePoint;
        int earnedPoints = finalAmount * 5 / 100;     /// 적립 포인트 게산 (%5)


        boolean couponUsed = false;
        boolean pointProcessingStarted = false;

        /// 결제 번호 생성
        String paymentId = String.format("P%s%03d", LocalDate.now()
                .format(DateTimeFormatter.BASIC_ISO_DATE), nextPaymentNumber);

        try {
            if (selectedCouponId != 0) {
                couponService.useCoupon(memberId, selectedCouponId); /// 쿠폰 사용
                couponUsed = true;
            }

            pointProcessingStarted = true;
            pointService.usePoint(memberId, usePoint, paymentId); /// 포인트 사용
            pointService.earnPoint(memberId, earnedPoints, paymentId); /// 포인트 적립


            Payment payment = new Payment(paymentId, selectedCouponId, originalAmount,
                    couponDiscount, usePoint, finalAmount, earnedPoints, pointService.getBalance(memberId),
                    paymentType, reservation);


            payments.put(paymentId, payment);
            paymentOwners.put(paymentId, memberId);
            nextPaymentNumber++;

            return payment;


        } catch (RuntimeException failure) { /// 오류 발생 삭제
            payments.remove(paymentId);
            paymentOwners.remove(paymentId);

            if (pointProcessingStarted) {
                pointService.rollbackPaymentPoints(memberId, paymentId, balanceBeforePayment);
            }
            if (couponUsed) {
                try {
                    couponService.restoreCoupon(memberId, selectedCouponId);
                } catch (RuntimeException rollbackFailure) {
                    failure.addSuppressed(rollbackFailure);
                }
            }
            throw failure;
        }
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
        Reservation reservation = payment.getReservation();


        if (payment.isRefunded()) { /// 환불 검사
            throw new IllegalArgumentException("이미 환불된 결제 입니다.");
        }

        if (reservation == null || reservation.getMember().getId() != memberId) {
            throw new IllegalArgumentException("본인의 예매에 연결된 결제만 환불할 수 있습니다.");
        }

        /// 상영 시작 20분 전까지만 환불 가능 (포인트, 쿠폰을 복구하기 전에 먼저 확인)
        reservationService.checkCancelTime(reservation);

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
        cancelReservation(reservation.getMember(), reservation.getId());

        return payment;
    }


    /// 예약 취소
    public void cancelReservation(Member member, Long reservationId) {

        Reservation reservation = reservationService.getReservation(reservationId);

        List<Payment> linkedPayments = getCompletedPayment(member.getId()).stream()
                .filter(payment -> payment.getReservation() != null)
                .filter(payment -> reservationId.equals(payment.getReservation().getId()))
                .toList();

        if (linkedPayments.size() > 1) {
            throw new IllegalStateException("이 예매에 결제가 여러 건 연결되어 있습니다.");
        }

        if (!linkedPayments.isEmpty()) {
            Payment payment = linkedPayments.get(0);
            refund(member.getId(), payment.getPaymentId());
        }
        reservation.cancel();
    }

}