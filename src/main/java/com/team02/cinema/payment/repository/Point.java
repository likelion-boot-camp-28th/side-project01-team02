package com.team02.cinema.payment.repository;

import java.time.LocalDateTime;

public class Point {

    // 포인트 내역 고유번호
    private int pointId;

    // 포인트를 소유한 회원번호
    private int memberId;

    // 관련 결제번호
    private String paymentId;

    // 포인트 변동량
    // 적립: +1000, 사용: -1000
    private int amount;

    // 포인트 처리 유형
    // EARN: 적립, USE: 사용
    // RESTORE: 사용 포인트 반환
    // REVOKE: 적립 포인트 회수
    private String type;

    // 포인트 처리 날짜 및 시간
    private LocalDateTime createdAt;


    public Point(int pointId, int memberId, String paymentId, int amount, String type, LocalDateTime createdAt) {
        this.pointId = pointId;
        this.memberId = memberId;
        this.paymentId = paymentId;
        this.amount = amount;
        this.type = type;
        this.createdAt = createdAt;
    }


}
