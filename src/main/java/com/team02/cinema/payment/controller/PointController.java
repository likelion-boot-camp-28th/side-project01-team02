package com.team02.cinema.payment.controller;

import com.team02.cinema.payment.repository.Point;
import com.team02.cinema.payment.service.PointService;

import java.util.List;

public class PointController {

    private final PointService pointService;

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }


    public void showPoints(int memberId) {

        int balance = pointService.getBalance(memberId);
        List<Point> histories = pointService.getPointHistories(memberId);

        System.out.println("\n===== 내 포인트 조회 =====");
        System.out.println("현재 포인트: " + balance);


        if (histories.isEmpty()) {  /// 가져온 해당 List 없는 경우
            System.out.println("포인트 이용 내역이 없습니다.");
        } else {
            histories.forEach(point -> {
                System.out.println("=== 포인트 사용 내역 ====");
                System.out.println("결제 번호: " + point.getPaymentId());
                System.out.println("결제 유형: " + point.getType());
                System.out.println("포인트 금액: " + point.getAmount());
                System.out.println("포인트 이용 날짜: " + point.getCreatedAt());

            });
        }
        System.out.println("=========================");
    }

}
