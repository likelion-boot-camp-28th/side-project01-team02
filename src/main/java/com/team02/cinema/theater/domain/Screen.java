package com.team02.cinema.theater.domain;

import com.team02.cinema.movie.domain.schedule.Schedule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

public class Screen {
    private final int row;
    private final int col;
    private final Map<Schedule, List<List<Seat>>> seats;

    public Screen(int row, int col){
        if (row < 1 || col < 1) {
            throw new IllegalArgumentException("잘못된 좌석 크기입니다.");
        }
        this.row = row;
        this.col = col;
        seats = new HashMap<>();
    }

    private List<List<Seat>> initializeSeats() {
        return IntStream.range(0, row)
                .mapToObj(i -> IntStream.range(0, col).mapToObj(j -> new Seat(i, j)).toList())
                .toList();
    }

    public void addSchedule(Schedule schedule){
        if (schedule == null) {
            throw new IllegalArgumentException("스케줄은 필수입니다.");
        }
        if (seats.putIfAbsent(schedule, initializeSeats()) != null) {
            throw new IllegalArgumentException("이미 등록된 스케줄입니다.");
        }
    }

    public List<List<Seat>> getSeats(Schedule schedule) {
        return Optional.ofNullable(seats.get(schedule))
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 스케줄입니다."));
    }

    public Seat getSeat(Schedule schedule, int row, int col) {
        if (row < 0 || row >= this.row || col < 0 || col >= this.col) {
            throw new IllegalArgumentException("존재하지 않는 좌석입니다.");
        }
        return getSeats(schedule).get(row).get(col);
    }

    public List<Seat> getAvailableSeats(Schedule schedule) {
        return getSeats(schedule).stream()
                .flatMap(List::stream)
                .filter(seat -> !seat.isReserved())
                .toList();
    }

    @Override
    public String toString() {
        return "좌석 수 : " + (row * col);
    }
}
