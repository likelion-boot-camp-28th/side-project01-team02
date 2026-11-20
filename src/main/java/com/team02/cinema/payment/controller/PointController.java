package com.team02.cinema.payment.controller;

import com.team02.cinema.payment.repository.Point;
import com.team02.cinema.payment.repository.PointType;
import com.team02.cinema.payment.service.PointService;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class PointController {

    private final PointService pointService;

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public void showMyPoints(int memberId) {

        int balance = pointService.getBalance(memberId);
        List<Point> histories = pointService.getUsePointHistoies(memberId);

        System.out.println("\n===== 내 포인트 조회 =====");
        System.out.println("현재 포인트: " + balance);


        if (histories.isEmpty()) {  /// 가져온 해당 List 없는 경우
            System.out.println("포인트 이용 내역이 없습니다.");
        } else {
            histories.forEach(point -> {
                System.out.println("=== 포인트 사용 내역 ====");
                System.out.println("  결제 번호: " + point.getPaymentId());
                System.out.println("  구분: " + getTypeName(point));
                System.out.println("  포인트 금액: " + point.getAmount());
                System.out.println("  포인트 이용 날짜: " + point.getCreatedAt());

            });
        }
        System.out.println("\n=========================");
    }

    /// 전체 포인트 이용 내역
    public void showPointHistory(int memberId) {
        List<Point> myPoints = pointService.getPointHistories(memberId);
        System.out.println("\n===== 포인트 이용 내역 =====");
        if (myPoints.isEmpty()) {
            System.out.println("포인트 이용 내역이 없습니다.");
        } else {
            myPoints.stream().forEach(point -> {
                System.out.println("---------------------------");
                System.out.println("  회원 번호: " + point.getMemberId());
                System.out.println("  결제 번호: " + point.getPaymentId());
                System.out.println("  구분: " + getTypeName(point));
                System.out.println("  포인트 금액: " + point.getAmount());
                System.out.println("  포인트 이용 날짜: " + point.getCreatedAt());
            });
        }
        System.out.println("\n=========================");

    }

    public void showEarnedPointHistory(int memberId) {
        System.out.println("\n===== 포인트 적립 내역 =====");

        List<Point> earnedPoints = pointService.getPointHistories(memberId)
                .stream().filter(point -> point.getType() == PointType.EARN).toList();
        int resultAmount = earnedPoints.stream().mapToInt(Point::getAmount).sum();

        if (earnedPoints.isEmpty()) return;
        System.out.println("  총 적립 포인트: " + resultAmount);
        earnedPoints.forEach(point -> {
            System.out.println("  결제 번호: " + point.getPaymentId());
            System.out.println("  적립 포인트: " + point.getAmount());

        });
        System.out.println("\n=========================");
    }

    /// 특정 결제의 포인트 내역 출력
    public void showPaymentPointHistory(int memberId, String paymentId) {

        List<Point> paymentPointHistories = pointService.getPaymentPointHistories(memberId, paymentId);
        System.out.println("\n===== 결제별 포인트 내역 =====");

        if (paymentPointHistories.isEmpty()) {
            System.out.println("해당 결제의 포인트 내역이 없습니다.");
            return;
        }
        paymentPointHistories.forEach(point -> {
            System.out.println("  구분: " + getTypeName(point));
            System.out.println("  포인트: " + point.getAmount());
        });
        System.out.println("\n=========================");

    }

    /// 포인트 한글 변환
    private String getTypeName(Point point) {
        return switch (point.getType()) {
            case USE -> "포인트 사용";
            case EARN -> "포인트 적립";
            case RESTORE -> "사용 포인트 반환";
            case REVOKE -> "적립 포인트 회수";
        };

    }

}
