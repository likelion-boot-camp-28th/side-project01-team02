package com.team02.cinema.reservation.domain;

import com.team02.cinema.member.domain.Member;
import com.team02.cinema.movie.domain.schedule.Schedule;
import com.team02.cinema.theater.domain.Seat;

import java.time.LocalDateTime;
import java.util.List;

public class Reservation {
    private final Long id;
    private final Member member;
    private final Schedule schedule;
    private final List<Seat> seats;
    private ReservationStatus status;
    private final LocalDateTime reservedAt;

    public Reservation(Long id, Member member, Schedule schedule, List<Seat> seats){
        this(id, member, schedule, seats, ReservationStatus.PENDING, LocalDateTime.now());
    }

    public Reservation(Long id, Member member, Schedule schedule, List<Seat> seats,
                       ReservationStatus status, LocalDateTime reservedAt){
        validate(member, schedule, seats);
        this.id = id;
        this.member = member;
        this.schedule = schedule;
        this.seats = List.copyOf(seats);
        this.status = status;
        this.reservedAt = reservedAt;
    }

    private void validate(Member member, Schedule schedule, List<Seat> seats){
        if (member == null) {
            throw new IllegalArgumentException("회원은 필수입니다.");
        }
        if (schedule == null) {
            throw new IllegalArgumentException("스케줄은 필수입니다.");
        }
        if (seats == null || seats.isEmpty()) {
            throw new IllegalArgumentException("좌석은 1개 이상 선택해야 합니다.");
        }
    }

    public synchronized void confirm(){
        if (status != ReservationStatus.PENDING) {
            throw new IllegalStateException("결제 대기 상태의 예약만 확정할 수 있습니다.");
        }
        status = ReservationStatus.CONFIRMED;
    }

    public synchronized void cancel(){
        if (status == ReservationStatus.CANCELED) {
            throw new IllegalStateException("이미 취소된 예약입니다.");
        }
        status = ReservationStatus.CANCELED;
    }

    public synchronized boolean isActive(){
        return status != ReservationStatus.CANCELED;
    }

    public synchronized ReservationStatus getStatus() {
        return status;
    }

    public LocalDateTime getReservedAt() {
        return reservedAt;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public List<Seat> getSeats() {
        return seats;
    }
}
