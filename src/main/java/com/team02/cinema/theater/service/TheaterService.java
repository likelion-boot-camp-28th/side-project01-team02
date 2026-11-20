package com.team02.cinema.theater.service;

import com.team02.cinema.movie.domain.schedule.Schedule;
import com.team02.cinema.theater.domain.Screen;
import com.team02.cinema.theater.domain.Seat;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TheaterService {
    private final Map<Integer, Screen> screens = new TreeMap<>();
    private final Map<Long, List<List<Seat>>> scheduleSeats = new HashMap<>();

    public void addScreen(int num, int row, int col){
        screens.put(num, new Screen(num, row, col));
    }

    public Collection<Screen> getScreens() {
        return List.copyOf(screens.values());
    }

    public Screen getScreen(int num){
        Screen screen = screens.get(num);
        if (screen == null) {
            throw new IllegalArgumentException("존재하지 않는 상영관입니다.");
        }
        return screen;
    }

    public List<List<Seat>> getSeats(Schedule schedule){
        return scheduleSeats.computeIfAbsent(schedule.getId(), id -> schedule.getScreen().createSeats());
    }

    public Seat getSeat(Schedule schedule, int row, int col){
        List<List<Seat>> seats = getSeats(schedule);
        if (row < 0 || row >= seats.size() || col < 0 || col >= seats.getFirst().size()) {
            throw new IllegalArgumentException("존재하지 않는 좌석입니다.");
        }
        return seats.get(row).get(col);
    }

    public void reserveSeat(Schedule schedule, int row, int col){
        getSeat(schedule, row, col).reserve();
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
