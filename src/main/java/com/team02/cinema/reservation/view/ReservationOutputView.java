package com.team02.cinema.reservation.view;

import com.team02.cinema.reservation.domain.Reservation;
import com.team02.cinema.theater.domain.Seat;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationOutputView {
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public void printReservation(Reservation reservation) {
        System.out.println(makeReservationText(reservation));
    }

    public void printReservationList(List<Reservation> reservations) {
        if (reservations.isEmpty()) {
            System.out.println("예매 내역이 없습니다.");
            return;
        }
        System.out.println("===== 예매 내역 =====");
        reservations.forEach(this::printReservation);
    }

    public void printReserved(Reservation reservation) {
        System.out.println("예매가 완료되었습니다. (결제 대기)");
        printReservation(reservation);
    }

    public void printCanceled() {
        System.out.println("예매가 취소되었습니다.");
    }

    public void printError(String message) {
        System.out.println("[ERROR] " + message);
    }

    private String makeReservationText(Reservation reservation) {
        String seats = reservation.getSeats().stream()
                .map(Seat::getName)
                .collect(Collectors.joining(", "));
        return "예매번호 " + reservation.getId() + " | "
                + reservation.getSchedule().getMovie().getTitle() + " | "
                + reservation.getSchedule().getScreen().getNum() + "관 | "
                + reservation.getSchedule().getStartTime().format(TIME_FORMAT) + " | "
                + seats + " | "
                + reservation.getStatus();
    }
}
