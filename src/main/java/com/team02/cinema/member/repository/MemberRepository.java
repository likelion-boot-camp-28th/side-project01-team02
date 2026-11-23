package com.team02.cinema.member.repository;

import com.team02.cinema.member.domain.Member;

import java.util.Optional;

public interface MemberRepository {
    void save(Member member);
    Optional<Member> findByLoginId(String loginId);
    void delete(String loginId);
}
