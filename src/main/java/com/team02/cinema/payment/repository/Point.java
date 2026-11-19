package com.team02.cinema.payment.repository;

import java.time.LocalDateTime;

public class Point {


    // 포인트를 소유한 회원번호
    private int memberId;

    // 관련 결제번호
    private String paymentId;

    // 포인트 변동량
    // 적립: +1000, 사용: -1000
    private int amount;

    // 포인트 처리 유형
    // EARN: 적립, USE: 사용
    // RESTORE: 환불 시 사용했던 포인트 반환
    // REVOKE: 환불 시 적립했던 포인트 회수
    private PointType type;

    // 포인트 처리 날짜 및 시간
    private LocalDateTime createdAt;


    public Point(PointType type, int amount, String paymentId, int memberId) {
        this.type = type;
        this.amount = amount;
        this.paymentId = paymentId;
        this.memberId = memberId;
    }


}
