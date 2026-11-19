package com.team02.cinema.movie.service;

// 스케줄 삭제 전에 "이 스케줄에 예매가 있는지"를 묻기 위한 인터페이스
// 실제 구현은 예매(reservation) 쪽에서 제공한다 (담당 C와 합의 필요)
@FunctionalInterface
public interface ReservationChecker {

    // 해당 스케줄에 유효한 예매(PENDING 또는 CONFIRMED)가 하나라도 있으면 true
    boolean hasReservation(Long scheduleId);
}
