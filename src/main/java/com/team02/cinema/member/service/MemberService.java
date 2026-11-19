package com.team02.cinema.member.service;

import com.team02.cinema.member.domain.Member;
import com.team02.cinema.member.repository.MemberRepository;

import java.time.LocalDate;
import java.util.Optional;

public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void signUp(String loginId, String password, String name, LocalDate birthDate){
        Optional<Member> found = memberRepository.findByLoginId(loginId);
        if (found.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
        Member member = new Member(loginId, password, name, birthDate);
        memberRepository.save(member);
    }

    public Member login(String loginId, String password){
        Optional<Member> found = memberRepository.findByLoginId(loginId);
        if (found.isEmpty()) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
        Member member = found.get();
        if (!member.matchesPassword(password)){
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
        return member;
    }
}
