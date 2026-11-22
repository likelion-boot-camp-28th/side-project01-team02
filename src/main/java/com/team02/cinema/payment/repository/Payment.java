package com.team02.cinema.payment.repository;

import com.team02.cinema.member.domain.Member;
import com.team02.cinema.reservation.domain.Reservation;

import java.time.LocalDateTime;
import java.util.Objects;

public class Payment {

//예매번호     : R20261119001
// 영화 제목    : 인터스텔라
// 상영 날짜    : 2026-11-21
// 상영 시간    : 18:30
// 상영관       : 3관
// 선택 좌석    : C5, C6
// 인원         : 일반 2명

    // 결제 고유번호
    // 예: "P20261119001"
    private final String paymentId;

    // 결제와 연결된 영화 예매 객체
    // 영화 제목, 상영관, 좌석 등의 정보를 가져올 때 사용
    private final Reservation reservation;


    private final Integer couponId;

    // 쿠폰 및 포인트 할인 전 티켓 총금액
    // 예: 24,000원
    private final int originalAmount;

    // 실제 적용된 쿠폰 할인금액
    // 예: 3,000원
    private final int couponDiscount;

    // 결제할 때 사용한 포인트
    // 예: 1,000P
    private final int usedPoints;

    // 쿠폰과 포인트를 제외한 실제 결제금액
    // 예: 24,000 - 3,000 - 1,000 = 20,000원
    private final int finalAmount;

    // 결제 완료 후 새롭게 적립되는 포인트
    // 예: 최종 결제금액 20,000원의 5% = 1,000P
    private final int earnedPoints;

    // 결제 완료 후 회원이 보유한 포인트 잔액
    // 예: 5,000P
    private final int remainingPoints;

    // 결제 수단
    // 예: "신용 / 체크카드", "계좌이체"
    private final PaymentType.BankType paymentMethod;

    // 실제 결제를 완료한 날짜와 시간
    private final LocalDateTime paidAt;

    // 현재 결제 상태
    // 예: "결제 완료" (false), "환불 완료" (true)
    private boolean refunded;


    public Payment(String paymentId, Integer couponId, int originalAmount, int couponDiscount, int usedPoints
            , int finalAmount, int earnedPoints, int remainingPoints, PaymentType.BankType paymentMethod, Reservation reservation) {
        this.paymentId = paymentId;
        this.couponId = couponId;
        this.originalAmount = originalAmount;
        this.couponDiscount = couponDiscount;
        this.usedPoints = usedPoints;
        this.finalAmount = finalAmount;
        this.earnedPoints = earnedPoints;
        this.remainingPoints = remainingPoints;
        this.paymentMethod = paymentMethod;
        this.paidAt = LocalDateTime.now();
        this.reservation = reservation;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public String getPaymentId() {
        return paymentId;
    }

    //    public Reservation getReservation() { return reservation; }
    public Integer getCouponId() {
        return couponId;
    }

    public int getOriginalAmount() {
        return originalAmount;
    }

    public int getCouponDiscount() {
        return couponDiscount;
    }

    public int getUsedPoints() {
        return usedPoints;
    }

    public int getFinalAmount() {
        return finalAmount;
    }

    public int getEarnedPoints() {
        return earnedPoints;
    }

    public int getRemainingPoints() {
        return remainingPoints;
    }

    public PaymentType.BankType getPaymentMethod() {
        return paymentMethod;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public boolean isRefunded() {
        return refunded;
    }

    public String getStatus() {
        return refunded ? "환불 완료" : "결제 완료";
    }


    /// 환불 상태 변경
    public void refund() {
        if (refunded) {
            throw new IllegalArgumentException("이미 환불된 결제 입니다.");
        }
        refunded = true;
    }


    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Payment payment = (Payment) object;
        return originalAmount == payment.originalAmount && couponDiscount == payment.couponDiscount && usedPoints == payment.usedPoints && finalAmount == payment.finalAmount && earnedPoints == payment.earnedPoints && remainingPoints == payment.remainingPoints && refunded == payment.refunded && Objects.equals(paymentId, payment.paymentId) && Objects.equals(couponId, payment.couponId) && paymentMethod == payment.paymentMethod && Objects.equals(paidAt, payment.paidAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentId, couponId, originalAmount, couponDiscount, usedPoints, finalAmount, earnedPoints, remainingPoints, paymentMethod, paidAt, refunded);
    }
}
