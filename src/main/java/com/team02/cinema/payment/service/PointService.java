package com.team02.cinema.payment.service;

import com.team02.cinema.payment.repository.Point;
import com.team02.cinema.payment.repository.PointType;

import java.util.*;

public class PointService {

    /// // 회원번호 -> 현재 포인트 잔액 : 간편 조회
    private final Map<Integer, Integer> resultBalances = new HashMap<>();


    /// // 모든 회원의 포인트 사용 내역
    private final List<Point> pointHistories = new ArrayList<>();


    /// 초기 포인트 생성
    public void setInitPoint(int memberId, int point) {

        if (point < 0) throw new IllegalArgumentException("초기 포인트는 음수일 수 없습니다.");

        resultBalances.put(memberId, point);

    }


    /// 현재 보유 포인트 조회
    public int getBalance(int memberId) {
        return resultBalances.getOrDefault(memberId, 0);  /// 없으면 0
    }

    /// 포인트 사용
    public void usePoint(int memberId, int amount, String paymentId) {

        if (amount < 0) { /// 포인트 사용 금액 0 보다 커야 된다.
            throw new IllegalArgumentException("포인트 사용금액을 잘못 입력하셨습니다.");

        }

        /// 소유한 포인트 < 사용할 포인트 액수
        if (getBalance(memberId) < amount) {
            throw new IllegalArgumentException("포인트가 부족 합니다.");

        }

        /// 포인트 사용 차감
        resultBalances.put(memberId, getBalance(memberId) - amount);
        addHistory(memberId, paymentId, -amount, PointType.USE);    /// 내역 등록

    }

    /// 포인트 적립
    public void earnPoint(int memberId, int amount, String paymentId) {
        if (amount < 0) {
            throw new IllegalArgumentException("잘못된 적립 포인트 입니다.");
        }

        /// 포인트 증가
        resultBalances.put(memberId, getBalance(memberId) + amount); /// 기존 값 + 포인트 값

        /// 적립 내역 저장
        addHistory(memberId, paymentId, amount, PointType.EARN);

    }


    /// 포인트 내역 저장
    private void addHistory(int memberId, String paymentId, int amount, PointType pointType) {

        Point point = new Point(memberId, paymentId, amount, pointType);
        pointHistories.add(point);  /// 포인트 사용 내역 추가.
    }

    /// 환불 시 사용 포인트 반환
    public int restorePoint(int memberId, String paymentId) {

        ///이미 반환된 결제인지 확인 ->> 환불 내역 존재 -> true
        boolean alreadyRestored = pointHistories.stream()
                .anyMatch(point -> point.getMemberId() == memberId && point.getPaymentId().equals(paymentId) && point.getType() == PointType.RESTORE);

        if (alreadyRestored) {
            throw new IllegalArgumentException("이미 반환된 포인트 입니다.");
        }


        int result = 0;
        result = pointHistories.stream()
                .filter(point -> point.getMemberId() == memberId)
                .filter(point -> point.getPaymentId().equals(paymentId))
                .filter(point -> point.getType() == PointType.USE)
                .mapToInt(point -> point.getAmount()).sum();

        if (result <= 0) {
            return 0;
        }

        resultBalances.put(memberId, getBalance(memberId) + result); /// 환불 포인트 값 반환.

        Point history = new Point(memberId, paymentId, result, PointType.RESTORE);
        pointHistories.add(history); /// 기록 저장.

        return result;
    }

    /// 환불 시 적립 포인트 회수
    public int revokePoints(int memberId, String paymentId) {

        /// 이미 회수 했는지
        boolean alreadyRevoked = pointHistories.stream()
                .anyMatch(point -> point.getMemberId() == memberId
                        && point.getPaymentId().equals(paymentId) && point.getType() == PointType.REVOKE);

        if (alreadyRevoked)
            throw new IllegalArgumentException("이미 회수된 포인트 입니다.");

        int revokeAmount = pointHistories.stream() ///  해당 결제에서 적립된 포인트
                .filter(point -> point.getMemberId() == memberId)
                .filter(point -> point.getPaymentId().equals(paymentId))
                .filter(point -> point.getType() == PointType.EARN)
                .mapToInt(point -> point.getAmount()).sum();

        if (revokeAmount == 0)
            return 0;

        if (getBalance(memberId) < revokeAmount) {
            throw new IllegalArgumentException("회수할 포인트가 부족합니다. ");
        }

        /// 적립 회수
        resultBalances.put(memberId, getBalance(memberId) - revokeAmount);
        addHistory(memberId, paymentId, -revokeAmount, PointType.REVOKE);

        return revokeAmount;
    }


    /// 회원별 포인트 내역 조회
    public List<Point> getPointHistories(int memberId) {

        return pointHistories.stream()
                .filter(point -> point.getMemberId() == memberId)
                .toList();   /// 해당 값 리스트 반환.
    }


    /// 결제번호별 포인트 내역 조회
    public List<Point> getPaymentPointHistories(int memberId, String paymentId) {

        return pointHistories.stream()
                .filter(point -> point.getMemberId() == memberId)
                .filter(point -> point.getPaymentId().equals(paymentId))
                .toList(); /// 해당 값 리스트 반환.

    }

    /// 회원별 포인트 사용 내역 조회
    public List<Point> getUsePointHistoies(int memberId) {

        return pointHistories.stream().filter(point -> point.getMemberId() == memberId)
                .filter(point -> point.getType() == PointType.USE)
                .sorted(Comparator.comparing(Point::getCreatedAt).reversed()).toList();

    }

}
