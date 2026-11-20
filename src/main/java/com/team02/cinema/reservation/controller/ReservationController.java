package com.team02.cinema.reservation.controller;

import com.team02.cinema.member.domain.Member;
import com.team02.cinema.movie.domain.schedule.Schedule;
import com.team02.cinema.reservation.domain.Reservation;
import com.team02.cinema.reservation.service.ReservationService;
import com.team02.cinema.reservation.view.ReservationInputView;
import com.team02.cinema.reservation.view.ReservationOutputView;
import com.team02.cinema.theater.view.TheaterView;

public class ReservationController {
    private final ReservationService reservationService;
    private final TheaterView theaterView;
    private final ReservationInputView inputView;
    private final ReservationOutputView outputView;

    public ReservationController(ReservationService reservationService, TheaterView theaterView,
                                 ReservationInputView inputView, ReservationOutputView outputView) {
        this.reservationService = reservationService;
        this.theaterView = theaterView;
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void showSeats(Schedule schedule) {
        theaterView.printSeats(schedule.getScreen().getSeats(), reservationService.getReservedSeats(schedule));
    }

    public Reservation reserve(Member member, Schedule schedule) {
        try {
            showSeats(schedule);
            Reservation reservation = reservationService.reserve(member, schedule, inputView.readSeatNames());
            outputView.printReserved(reservation);
            return reservation;
        } catch (IllegalArgumentException | IllegalStateException e) {
            outputView.printError(e.getMessage());
            return null;
        }
    }

    public void showMyReservations(Member member) {
        outputView.printReservationList(reservationService.getMemberReservations(member));
    }

    public void cancel(Member member) {
        try {
            showMyReservations(member);
            reservationService.cancel(member, inputView.readReservationId());
            outputView.printCanceled();
        } catch (IllegalArgumentException | IllegalStateException e) {
            outputView.printError(e.getMessage());
        }
    }
}
