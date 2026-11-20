package com.team02.cinema.reservation.service;

import com.team02.cinema.member.domain.Member;
import com.team02.cinema.movie.domain.schedule.Schedule;
import com.team02.cinema.reservation.domain.Reservation;
import com.team02.cinema.reservation.repository.ReservationRepository;
import com.team02.cinema.theater.domain.Seat;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReservationService {
    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository){
        this.reservationRepository = reservationRepository;
    }

    public Reservation reserve(Member member, Schedule schedule, List<Seat> seats){
        Reservation reservation = new Reservation(null, member, schedule, seats);
        Set<Seat> screenSeats = new HashSet<>();
        schedule.getScreen().getSeats().forEach(screenSeats::addAll);
        if (!screenSeats.containsAll(seats) || new HashSet<>(seats).size() != seats.size()) {
            throw new IllegalArgumentException("선택할 수 없는 좌석입니다.");
        }
        if (seats.stream().anyMatch(getReservedSeats(schedule)::contains)) {
            throw new IllegalStateException("이미 예약된 좌석입니다.");
        }
        return reservationRepository.save(reservation);
    }

    public void cancel(Long id){
        if (!reservationRepository.deleteById(id)) {
            throw new IllegalArgumentException("존재하지 않는 예약입니다.");
        }
    }

    public Set<Seat> getReservedSeats(Schedule schedule){
        Set<Seat> reserved = new HashSet<>();
        reservationRepository.findByScheduleId(schedule.getId())
                .forEach(reservation -> reserved.addAll(reservation.getSeats()));
        return reserved;
    }

    public List<Seat> getAvailableSeats(Schedule schedule){
        Set<Seat> reserved = getReservedSeats(schedule);
        return schedule.getScreen().getSeats().stream()
                .flatMap(List::stream)
                .filter(seat -> !reserved.contains(seat))
                .toList();
    }
}
