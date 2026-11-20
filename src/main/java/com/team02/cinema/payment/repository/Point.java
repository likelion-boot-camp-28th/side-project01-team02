package com.team02.cinema.payment.repository;


import java.time.LocalDateTime;
import java.util.Objects;

public class Point  {


    // 포인트를 소유한 회원번호
    private int memberId;

    // 관련 결제번호
    private String paymentId;

    // 포인트 변동량
    private int amount;

    // 포인트 처리 유형
    // EARN: 적립, USE: 사용
    // RESTORE: 환불 시 사용했던 포인트 반환
    // REVOKE: 환불 시 적립했던 포인트 회수
    private PointType type;

    // 포인트 처리 날짜 및 시간
    private LocalDateTime createdAt;

    public int getMemberId() {
        return memberId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public int getAmount() {
        return amount;
    }

    public PointType getType() {
        return type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Point(int memberId, String paymentId, int amount, PointType type) {
        this.type = type;
        this.amount = amount;
        this.paymentId = paymentId;
        this.memberId = memberId;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Point point = (Point) object;
        return memberId == point.memberId && amount == point.amount && Objects.equals(paymentId, point.paymentId) && type == point.type && Objects.equals(createdAt, point.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, paymentId, amount, type, createdAt);
    }



}
