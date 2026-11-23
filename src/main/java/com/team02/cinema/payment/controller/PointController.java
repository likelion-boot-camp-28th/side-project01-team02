package com.team02.cinema.payment.controller;

import com.team02.cinema.payment.repository.Point;
import com.team02.cinema.payment.repository.PointType;
import com.team02.cinema.payment.service.PointService;
import org.w3c.dom.ls.LSOutput;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class PointController {

    private final PointService pointService;
    private final Scanner sc = new Scanner(System.in);

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public void currentPoints(long memberId){
        int balance = pointService.getBalance(memberId);
        List<Point> histories = pointService.getUsePointHistoies(memberId);

        System.out.println("\n===== 내 포인트 조회 =====");
        System.out.println("현재 포인트: " + balance);
        System.out.println("\n========================");

    }

    public void showUsePoints(long memberId) {

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
                System.out.println("  구분: " + getTypeName(point) + " | " + " 금액: " + point.getAmount());
                System.out.println("  포인트 이용 날짜: " + point.getCreatedAt().format(formatter));

            });
        }
        System.out.println("\n=========================");
    }

    /// 전체 포인트 이용 내역
    public void showPointHistory(long memberId) {
        List<Point> myPoints = pointService.getPointHistories(memberId);
        System.out.println("\n===== 포인트 이용 내역 =====");
        if (myPoints.isEmpty()) {
            System.out.println("포인트 이용 내역이 없습니다.");
        } else {
            myPoints.stream().forEach(point -> {
                System.out.println("---------------------------");
                System.out.println("  회원 번호: " + point.getMemberId());
                System.out.println("  결제 번호: " + point.getPaymentId());
                System.out.println("  구분: " + getTypeName(point) + " | " + " 금액: " + point.getAmount());
                System.out.println("  포인트 이용 날짜: " + point.getCreatedAt().format(formatter));
            });
        }
        System.out.println("\n=========================");

    }

    public void showEarnedPointHistory(long memberId) {
        System.out.println("\n===== 포인트 적립 내역 =====");

        List<Point> earnedPoints = pointService.getPointHistories(memberId)
                .stream().filter(point -> point.getType() == PointType.EARN).toList();
        int resultAmount = earnedPoints.stream().mapToInt(Point::getAmount).sum();

        if (earnedPoints.isEmpty()){
            System.out.println("포인트 적립 내역이 없습니다.");
            return;
        }

        System.out.println("  총 적립 포인트: " + resultAmount);
        earnedPoints.forEach(point -> {
            System.out.println("  결제 번호: " + point.getPaymentId());
            System.out.println("  적립 포인트: " + point.getAmount());

        });
        System.out.println("\n=========================");
    }

    /// 특정 결제의 포인트 내역 출력
    public void showPaymentPointHistory(long memberId, String paymentId) {

        List<Point> paymentPointHistories = pointService.getPaymentPointHistories(memberId, paymentId);
        System.out.println("\n===== 결제별 포인트 내역 =====");

        if (paymentPointHistories.isEmpty()) {
            System.out.println("해당 결제의 포인트 내역이 없습니다.");
            return;
        }
        paymentPointHistories.forEach(point -> {
            System.out.println("  구분: " + getTypeName(point) + " | " + " 포인트: " + point.getAmount());
            System.out.println();
        });
        System.out.println("\n=========================");

    }

    /// 포인트 한글 변환
    private String getTypeName(Point point) {
        return switch (point.getType()) {
            case USE -> "포인트 사용";
            case EARN -> "포인트 적립";
            case RESTORE -> "사용 포인트 반환";
            case REVOKE -> "적립 포인트 회수(환불)";
        };

    }

    public int usePoint(long memberId, int points) {

        int balance = pointService.getBalance(memberId);

        System.out.printf("보유 포인트: %,dP%n", balance);

        if (points < 0 || points > balance) {
            throw new IllegalArgumentException(balance == 0  ? "보유 포인트가 없습니다. 0을 입력해 주세요."  : String.format("0부터 %,d까지 입력해 주세요.", balance)
            );
        }

        return points;
    }

}
