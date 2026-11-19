package com.team02.cinema.theater.service;

import com.team02.cinema.member.domain.Member;
import com.team02.cinema.movie.domain.schedule.Schedule;
import com.team02.cinema.theater.domain.Screen;
import com.team02.cinema.theater.domain.Seat;
import com.team02.cinema.theater.repository.ScreenRepository;

import java.util.List;
import java.util.Map;

public class TheaterService {
    private final ScreenRepository screenRepository;

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
        getScreen(num).addSchedule(schedule);
    }

    public Seat getSeat(int num, Schedule schedule, int row, int col){
        return getScreen(num).getSeat(schedule, row, col);
    }

    public void reserveSeat(int num, Schedule schedule, int row, int col, Member member){
        getSeat(num, schedule, row, col).reserve(member);
    }

    public void cancelSeat(int num, Schedule schedule, int row, int col){
        getSeat(num, schedule, row, col).cancel();
    }

    public List<Seat> getAvailableSeats(int num, Schedule schedule){
        return getScreen(num).getAvailableSeats(schedule);
    }

    public List<List<Seat>> getScreenSeats(int num, Schedule schedule){
        return getScreen(num).getSeats(schedule);
    }
}
