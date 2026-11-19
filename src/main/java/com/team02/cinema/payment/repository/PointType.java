package com.team02.cinema.payment.repository;

public enum PointType {

    EARN,     // 결제 완료 후 포인트 적립
    USE,      // 결제 시 포인트 사용
    RESTORE,  // 환불 시 사용했던 포인트 반환
    REVOKE    // 환불 시 적립했던 포인트 회수


}
