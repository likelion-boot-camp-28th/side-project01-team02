package com.team02.cinema.member.controller;

import com.team02.cinema.member.domain.Member;
import com.team02.cinema.member.service.MemberService;

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
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
