package com.team02.cinema;

import com.team02.cinema.member.controller.MemberController;
import com.team02.cinema.member.repository.MemberRepository;
import com.team02.cinema.member.repository.MemoryMemberRepository;
import com.team02.cinema.member.service.MemberService;


public class Application {
    public static void main(String[] args) {
        MemberRepository memberRepository = new MemoryMemberRepository();
        MemberService memberService = new MemberService(memberRepository);
        MemberController memberController = new MemberController(memberService);

    }
}
