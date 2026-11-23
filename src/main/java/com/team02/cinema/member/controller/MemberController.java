package com.team02.cinema.member.controller;

import com.team02.cinema.common.view.OutputView;
import com.team02.cinema.member.domain.Member;
import com.team02.cinema.member.service.MemberService;

import java.time.LocalDate;
import java.util.Optional;

public class MemberController {
    private final MemberService memberService;
    private Member loginMember;
    private final OutputView outputView;



    public MemberController(MemberService memberService, OutputView outputView) {
        this.memberService = memberService;
        this.outputView = outputView;
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
            outputView.printWelcome(loginMember.getName());
        } catch (IllegalArgumentException e) {
            outputView.printError(e.getMessage());
        }
    }

    public void signUp(String loginId, String password, String name, LocalDate birthDate){
        try {
            memberService.signUp(loginId, password, name, birthDate);
            outputView.printSignUpSuccess();
        } catch (IllegalArgumentException e) {
            outputView.printError(e.getMessage());
        }
    }

    public Optional<Member> getLoginMember(){
        return Optional.ofNullable(loginMember);
    }

    public void withdraw(){
        try {
            memberService.withdraw(loginMember);
            logout();
            outputView.printWithdraw();
        } catch (IllegalStateException e) {
            outputView.printError(e.getMessage());
        }
    }
}
