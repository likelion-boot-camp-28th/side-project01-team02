package com.team02.cinema.member.service;

import com.team02.cinema.member.domain.Member;

public interface MemberReservationChecker {
    boolean hasConfirmedFutureReservation(Member member);


}
