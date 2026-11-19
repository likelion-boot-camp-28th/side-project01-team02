package com.team02.cinema.payment.service;

import com.team02.cinema.payment.repository.Point;
import com.team02.cinema.payment.repository.PointType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PointService {

    /// // 회원번호 -> 현재 포인트 잔액 : 간편 조회
    private final Map<Integer, Integer> resultBalances = new HashMap<>();


    /// // 모든 회원의 포인트 사용 내역
    private final List<Point> pointHistories = new ArrayList<>();


    /// 초기 포인트 생성
    public void setInitPoint(int memberId, int point) {

        if (point < 0)
            System.out.println("초기 포인트는 음수일 수 없습니다.");

        resultBalances.put(memberId, point);


    }


    /// 현재 보유 포인트 조회
    public int getBalance(int memberId) {
        return resultBalances.getOrDefault(memberId, 0);  /// 없으면 0
    }

    /// 포인트 사용
    public void usePoint(int memberId, int amount, String paymentId) {

        if (amount < 0) { /// 포인트 사용 금액 0 보다 커야 된다.
            System.out.println("포인트 사용금액을 잘못 입력하셨습니다.");
            return;
        }

        /// 소유한 포인트 < 사용할 포인트 액수
        if (getBalance(memberId) < amount) {
            System.out.println("포인트가 부족 합니다.");
            return;
        }

        /// 포인트 사용 차감
        resultBalances.put(memberId, getBalance(memberId) - amount);
        addHistory(memberId, paymentId, -amount, PointType.USE);    /// 내역 등록

    }

    /// 포인트 적립
    public void earnPoint(int memberId, int amount, String paymentId) {
        if (amount < 0) {
            System.out.println("잘못된 적립 포인트 입니다.");
        }

        /// 포인트 증가
        resultBalances.put(memberId, getBalance(memberId) + amount); /// 기존 값 + 포인트 값

        /// 적립 내역 저장
        addHistory(memberId, paymentId, amount, PointType.EARN);

    }


    /// 포인트 사용/적립 ->> 내역 추가
    private void addHistory(int memberId, String paymentId, int amount, PointType pointType) {

        Point point = new Point(pointType, amount, paymentId, memberId);
        pointHistories.add(point);  /// 포인트 사용 내역 추가.
    }

    /// 포인트 내역 조회
    public void showPoint(int memberId) {

        System.out.println("\n===== 내 포인트 조회 =====");
        System.out.println("현재 포인트: " + getBalance(memberId));
        boolean found = false;

        for (Point pointHistory : pointHistories) {

            if (pointHistory.getMemberId() == memberId) {
                found = true;
                System.out.println("=== 포인트 사용 내역 ====");
                System.out.println("결제 번호: " + pointHistory.getPaymentId());
                System.out.println("결제 유형: " + pointHistory.getType());
                System.out.println("포인트 금액: " + pointHistory.getAmount());
                System.out.println("포인트 이용 날짜: " + pointHistory.getCreatedAt());

            }
        }
        if (!found)
            System.out.println("포인트 이용 내역이 없습니다.");
        System.out.println("=========================");

    }


    /// 환불 시 사용 포인트 반환
    public void restorePoint(int memberId, String paymentId) {

        int result = 0;
        for (Point pointHistory : pointHistories) {

            if (pointHistory.getPaymentId().equals(paymentId)) {    /// 해당 포인트 내역 찾기
                result += pointHistory.getAmount();
            }

        }
        resultBalances.put(memberId, getBalance(memberId) + result); /// 환불 포인트 값 반환.
    }

    /// 환불 시 적립 포인트 회수
}
