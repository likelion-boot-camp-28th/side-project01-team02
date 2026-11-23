package com.team02.cinema.reservation.service;

import com.team02.cinema.member.domain.Member;
import com.team02.cinema.movie.domain.schedule.Schedule;
import com.team02.cinema.reservation.domain.Reservation;
import com.team02.cinema.reservation.domain.ReservationStatus;
import com.team02.cinema.reservation.repository.ReservationRepository;
import com.team02.cinema.theater.domain.Seat;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;

public class ReservationService {
    private static final int MAX_SEATS = 4;
    private static final int CANCEL_LIMIT_MINUTES = 20;

    private final ReservationRepository reservationRepository;
    private final Map<Long, Object> locks = new ConcurrentHashMap<>();

    public ReservationService(ReservationRepository reservationRepository){
        this.reservationRepository = reservationRepository;
    }

    public Reservation reserve(Member member, Schedule schedule, String seatNames){
        if (seatNames == null || seatNames.isBlank()) {
            throw new IllegalArgumentException("좌석을 입력해 주세요.");
        }
        List<Seat> seats = Arrays.stream(seatNames.split(","))
                .map(name -> schedule.getScreen().getSeat(name))
                .toList();
        return reserve(member, schedule, seats);
    }

    public Reservation reserve(Member member, Schedule schedule, List<Seat> seats){
        Reservation reservation = new Reservation(null, member, schedule, seats);
        if (seats.size() > MAX_SEATS) {
            throw new IllegalArgumentException("한 번에 최대 " + MAX_SEATS + "석까지 예매할 수 있습니다.");
        }
        if (!schedule.getStartTime().isAfter(LocalDateTime.now())) {
            throw new IllegalStateException("이미 시작한 스케줄은 예매할 수 없습니다.");
        }
        Set<Seat> screenSeats = new HashSet<>();
        schedule.getScreen().getSeats().forEach(screenSeats::addAll);
        if (!screenSeats.containsAll(seats) || new HashSet<>(seats).size() != seats.size()) {
            throw new IllegalArgumentException("선택할 수 없는 좌석입니다.");
        }
        synchronized (locks.computeIfAbsent(schedule.getId(), id -> new Object())) {
            Set<Seat> reserved = getReservedSeats(schedule);
            if (seats.stream().anyMatch(reserved::contains)) {
                throw new IllegalStateException("이미 예약된 좌석입니다.");
            }
            return reservationRepository.save(reservation);
        }
    }

    public Reservation getReservation(Long id){
        return reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));
    }

    public List<Reservation> getMemberReservations(Member member){
        return reservationRepository.findByMember(member);
    }

    public boolean hasUpcomingConfirmed(Member member){
        return getMemberReservations(member).stream()
                .anyMatch(reservation -> reservation.getStatus() == ReservationStatus.CONFIRMED
                        && reservation.getSchedule().getStartTime().isAfter(LocalDateTime.now()));
    }

    public void confirm(Long id){
        getReservation(id).confirm();
    }

    public void cancelBySystem(Long id){
        getReservation(id).cancel();
    }

    public void cancel(Member member, Long id){
        Reservation reservation = getReservation(id);
        if (!reservation.getMember().getLoginId().equals(member.getLoginId())) {
            throw new IllegalArgumentException("본인의 예매만 취소할 수 있습니다.");
        }
        checkCancelTime(reservation);
        reservation.cancel();
    }

    // 상영 시작 20분 전이 지났으면 취소(환불)할 수 없다. (예매 취소와 환불에서 같이 사용)
    public void checkCancelTime(Reservation reservation){
        LocalDateTime limit = reservation.getSchedule().getStartTime().minusMinutes(CANCEL_LIMIT_MINUTES);
        if (reservation.isActive() && LocalDateTime.now().isAfter(limit)) {
            throw new IllegalStateException("상영 시작 " + CANCEL_LIMIT_MINUTES + "분 전까지만 취소할 수 있습니다.");
        }
    }

    public boolean hasReservation(Long scheduleId){
        return reservationRepository.findByScheduleId(scheduleId).stream().anyMatch(Reservation::isActive);
    }

    public Set<Seat> getReservedSeats(Schedule schedule){
        Set<Seat> reserved = new HashSet<>();
        reservationRepository.findByScheduleId(schedule.getId()).stream()
                .filter(Reservation::isActive)
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
