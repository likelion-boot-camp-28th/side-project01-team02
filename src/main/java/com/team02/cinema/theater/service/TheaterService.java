package com.team02.cinema.theater.service;

import com.team02.cinema.member.domain.Member;
import com.team02.cinema.movie.domain.schedule.Schedule;
import com.team02.cinema.theater.domain.Screen;
import com.team02.cinema.theater.domain.Seat;
import com.team02.cinema.theater.repository.ScreenRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TheaterService {
    private final ScreenRepository screenRepository;
    private final Map<Schedule, List<List<Seat>>> scheduleSeats = new HashMap<>();

    public TheaterService(ScreenRepository screenRepository){
        this.screenRepository = screenRepository;
    }

    public void addScreen(int num, int row, int col){
        screenRepository.save(num, new Screen(row, col));
    }

    public Map<Integer, Screen> getScreens() {
        return screenRepository.findAll();
    }

    public Screen getScreen(int num){
        return screenRepository.findByNum(num)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상영관입니다."));
    }

    public void addSchedule(int num, Schedule schedule){
        if (schedule == null) {
            throw new IllegalArgumentException("스케줄은 필수입니다.");
        }
        if (scheduleSeats.containsKey(schedule)) {
            throw new IllegalArgumentException("이미 등록된 스케줄입니다.");
        }
        scheduleSeats.put(schedule, getScreen(num).createSeats());
    }

    public List<List<Seat>> getSeats(Schedule schedule){
        return Optional.ofNullable(scheduleSeats.get(schedule))
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 스케줄입니다."));
    }

    public Seat getSeat(Schedule schedule, int row, int col){
        List<List<Seat>> seats = getSeats(schedule);
        if (row < 0 || row >= seats.size() || col < 0 || col >= seats.getFirst().size()) {
            throw new IllegalArgumentException("존재하지 않는 좌석입니다.");
        }
        return seats.get(row).get(col);
    }

    public void reserveSeat(Schedule schedule, int row, int col, Member member){
        getSeat(schedule, row, col).reserve(member);
    }

    public void cancelSeat(Schedule schedule, int row, int col){
        getSeat(schedule, row, col).cancel();
    }

    public List<Seat> getAvailableSeats(Schedule schedule){
        return getSeats(schedule).stream()
                .flatMap(List::stream)
                .filter(seat -> !seat.isReserved())
                .toList();
    }
}
