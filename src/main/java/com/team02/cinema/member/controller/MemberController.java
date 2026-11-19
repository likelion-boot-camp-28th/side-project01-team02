package com.team02.cinema.member.controller;

import com.team02.cinema.member.domain.Member;

public class MemberController {
    private Member loginMember;

    public void logout(){
        loginMember = null;
    }

    public boolean canEnterAdminMenu(){
        if (loginMember == null){
            return false;
        }
        return loginMember.isAdmin();
    }
}
