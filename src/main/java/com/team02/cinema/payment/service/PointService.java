package com.team02.cinema.payment.service;

import com.team02.cinema.member.domain.Member;
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

        addHistory(memberId, paymentId, amount, PointType.USE);


    }

    /// 포인트 사용 ->> 내역 추가
    private void addHistory(int memberId, String paymentId, int amount, PointType pointType) {

        Point point = new Point(pointType, amount, paymentId, memberId);
        pointHistories.add(point);  /// 포인트 사용 내역 추가.
    }

    /// 포인트 내역 조회
    public void showPoint(int memberId) {

        for (Point pointHistory : pointHistories) {

        }

        System.out.println("\\n===== 내 포인트 조회 =====");
        System.out.println("현재 포인트: " + getBalance(memberId));
        System.out.println("=== 포인트 사용 내역 ====");
        System.out.println("결제 번호: "  );


    }

}
