package com.team02.cinema.reservation.repository;

import com.team02.cinema.member.domain.Member;
import com.team02.cinema.reservation.domain.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    List<Reservation> findAll();

    Optional<Reservation> findById(Long id);

    List<Reservation> findByMember(Member member);

    boolean deleteById(Long id);
}
