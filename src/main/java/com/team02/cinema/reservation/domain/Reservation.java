package com.team02.cinema.reservation.domain;

import com.team02.cinema.member.domain.Member;
import com.team02.cinema.movie.domain.schedule.Schedule;
import com.team02.cinema.theater.domain.Seat;

import java.util.List;

public class Reservation {
    private final Long id;
    private final Member member;
    private final Schedule schedule;
    private final List<Seat> seats;

    public Reservation(Long id, Member member, Schedule schedule, List<Seat> seats){
        validate(member, schedule, seats);
        this.id = id;
        this.member = member;
        this.schedule = schedule;
        this.seats = List.copyOf(seats);
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
