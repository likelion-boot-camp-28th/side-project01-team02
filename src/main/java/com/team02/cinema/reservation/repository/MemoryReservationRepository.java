package com.team02.cinema.reservation.repository;

import com.team02.cinema.member.domain.Member;
import com.team02.cinema.reservation.domain.Reservation;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MemoryReservationRepository implements ReservationRepository {
    private final Map<Long, Reservation> reservations = new LinkedHashMap<>();
    private long lastId = 0;

    @Override
    public Reservation save(Reservation reservation) {
        Reservation saved = reservation;
        if (reservation.getId() == null) {
            saved = new Reservation(lastId + 1, reservation.getMember(),
                    reservation.getSchedule(), reservation.getSeats(), reservation.getStatus());
        }
        lastId = Math.max(lastId, saved.getId());
        reservations.put(saved.getId(), saved);
        return saved;
    }

    @Override
    public List<Reservation> findAll() {
        return List.copyOf(reservations.values());
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return Optional.ofNullable(reservations.get(id));
    }

    @Override
    public List<Reservation> findByMember(Member member) {
        return reservations.values().stream()
                .filter(reservation -> reservation.getMember().equals(member))
                .toList();
    }

    @Override
    public List<Reservation> findByScheduleId(Long scheduleId) {
        return reservations.values().stream()
                .filter(reservation -> reservation.getSchedule().getId().equals(scheduleId))
                .toList();
    }
}
