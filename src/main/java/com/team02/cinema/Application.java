package com.team02.cinema;

import com.team02.cinema.common.view.InputView;
import com.team02.cinema.common.view.OutputView;
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
        OutputView outputView = new OutputView();

        boolean running = true;
        while(running){
            outputView.printMenu();
            String choice = inputView.readMenu();

            switch (choice){
                case "0" -> {
                    outputView.printExit();
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
                        outputView.printInvalidBirthDate();
                    }
                }
                case "2" -> {
                    String id = inputView.readLoginId();
                    String password = inputView.readPassword();
                    memberController.login(id,password);
                }
                case "3" -> {
                    if (memberController.getLoginMember().isEmpty()){
                        outputView.printNotLoggedIn();
                    }else{
                        memberController.logout();
                        outputView.printLogout();
                    }
                }
                default -> outputView.printInvalidMenu();
            }

        }
    }
}
