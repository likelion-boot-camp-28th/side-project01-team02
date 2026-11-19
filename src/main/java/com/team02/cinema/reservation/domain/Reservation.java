package com.team02.cinema.reservation.domain;

import com.team02.cinema.movie.domain.Movie.Movie;
import com.team02.cinema.movie.domain.schedule.Schedule;
import com.team02.cinema.theater.domain.Screen;
import com.team02.cinema.theater.domain.Seat;

public class Reservation {
    private Screen screen;
    private Seat seat;
    private Movie movie;
    private Schedule schedule;

    public Reservation(Screen screen, Seat seat, Movie movie, Schedule schedule){

    }
}
