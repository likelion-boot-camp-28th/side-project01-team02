package com.team02.cinema;

import com.team02.cinema.member.controller.MemberController;
import com.team02.cinema.member.repository.MemberRepository;
import com.team02.cinema.member.repository.MemoryMemberRepository;
import com.team02.cinema.member.service.MemberService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;


public class Application {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        MemberRepository memberRepository = new MemoryMemberRepository();
        MemberService memberService = new MemberService(memberRepository);
        MemberController memberController = new MemberController(memberService);


        boolean running = true;
        while(running){
            System.out.println("1. 회원가입");
            System.out.println("2. 로그인");
            System.out.println("3. 로그아웃");
            System.out.println("0. 종료");
            System.out.print("번호를 입력하세요 : ");

            String choice = sc.nextLine();

            switch (choice){
                case "0" -> {
                    System.out.println("시스템이 종료됩니다.");
                    running = false;
                }
                case "1" -> {
                    System.out.print("아이디를 입력하세요 : ");
                    String id = sc.nextLine();
                    System.out.print("비밀번호를 입력하세요 : ");
                    String password = sc.nextLine();
                    System.out.print("이름을 입력하세요 : ");
                    String name = sc.nextLine();
                    System.out.print("생년월일을 입력하세요(yyyy-MM-dd) : ");
                    try {
                        LocalDate birth = LocalDate.parse(sc.nextLine());
                        memberController.signUp(id,password,name,birth);
                    } catch (DateTimeParseException e) {
                        System.out.println("생년월일 형식이 올바르지 않습니다. (예 : 2000-01-15)");
                    }

                }
                case "2" -> {
                    System.out.print("아이디를 입력하세요 : ");
                    String loginId = sc.nextLine();
                    System.out.print("비밀번호를 입력하세요 : ");
                    String password = sc.nextLine();
                    memberController.login(loginId,password);
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
