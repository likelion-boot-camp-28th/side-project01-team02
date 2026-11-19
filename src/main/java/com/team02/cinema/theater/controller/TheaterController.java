package com.team02.cinema.theater.controller;

import com.team02.cinema.movie.domain.schedule.Schedule;
import com.team02.cinema.theater.repository.MemoryScreenRepository;
import com.team02.cinema.theater.service.TheaterService;
import com.team02.cinema.theater.view.TheaterView;

public class TheaterController {
    private final TheaterView view;
    private final TheaterService theater;

    public TheaterController(){
        view = new TheaterView();
        theater = new TheaterService(new MemoryScreenRepository());
    }

    public void addScreen(int num, int row, int col){
        theater.addScreen(num, row, col);
    }

    public void addSchedule(int num, Schedule schedule){
        theater.addSchedule(num, schedule);
    }

    public void viewScreenSeats(int num, Schedule schedule){
        view.printSeats(theater.getScreenSeats(num, schedule));
    }
    public void viewAllScreens(){
        view.printScreens(theater);
    }

    public void seatLookup(int num, Schedule schedule, int row, int col){
        view.printSeat(theater.getSeat(num, schedule, row, col));
    }
}
