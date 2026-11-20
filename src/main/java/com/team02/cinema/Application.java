package com.team02.cinema;

import com.team02.cinema.common.view.InputView;
import com.team02.cinema.member.controller.MemberController;
import com.team02.cinema.member.repository.MemberRepository;
import com.team02.cinema.member.repository.MemoryMemberRepository;
import com.team02.cinema.member.service.MemberService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;


public class Application {
    public static void main(String[] args) {
        MemberRepository memberRepository = new MemoryMemberRepository();
        MemberService memberService = new MemberService(memberRepository);
        MemberController memberController = new MemberController(memberService);
        InputView inputView = new InputView();


        boolean running = true;
        while(running){
            System.out.println("1. 회원가입");
            System.out.println("2. 로그인");
            System.out.println("3. 로그아웃");
            System.out.println("0. 종료");
            String choice = inputView.readMenu();

            switch (choice){
                case "0" -> {
                    System.out.println("시스템이 종료됩니다.");
                    running = false;
                }
                case "1" -> {
                    String id = inputView.readLoginId();
                    String password = inputView.readPassword();
                    String name = inputView.readName();
                    try {
                        LocalDate birth = inputView.readBirthDate();
                        memberController.signUp(id,password,name,birth);
                    } catch (DateTimeParseException e) {
                        System.out.println("생년월일 형식이 올바르지 않습니다. (예 : 2000-01-15)");
                    }
                }
                case "2" -> {
                    String id = inputView.readLoginId();
                    String password = inputView.readPassword();
                    memberController.login(id,password);
                }
                case "3" -> {
                    if (memberController.getLoginMember().isEmpty()){
                        System.out.println("로그인 상태가 아닙니다.");
                    }else{
                        memberController.logout();
                        System.out.println("로그아웃되었습니다.");
                    }
                }
                default -> System.out.println("잘못입력하셨습니다.");
            }

        }
    }
}
