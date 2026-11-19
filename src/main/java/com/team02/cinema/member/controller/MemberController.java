package com.team02.cinema.member.controller;

import com.team02.cinema.member.domain.Member;
import com.team02.cinema.member.service.MemberService;

import java.time.LocalDate;

public class MemberController {
    private final MemberService memberService;
    private Member loginMember;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    public void logout(){
        loginMember = null;
    }

    public boolean canEnterAdminMenu(){
        if (loginMember == null){
            return false;
        }
        return loginMember.isAdmin();
    }

    public void login(String loginId, String password){
        try {
            loginMember = memberService.login(loginId, password);
            System.out.println(loginMember.getName() + "님, 환영합니다!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void signUp(String loginId, String password, String name, LocalDate birthDate){
        try {
            memberService.signUp(loginId, password, name, birthDate);
            System.out.println("회원가입 되었습니다!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
