package com.team02.cinema.common.view;

public class OutputView {
    public void printMenu(){
        System.out.println("1. 회원가입");
        System.out.println("2. 로그인");
        System.out.println("3. 로그아웃");
        System.out.println("0. 종료");
    }

    public void printExit(){
        System.out.println("시스템이 종료됩니다.");
    }

    public void printLogout(){
        System.out.println("로그아웃되었습니다.");
    }


    public void printError(String message){
        System.out.println(message);
    }

    public void printWelcome(String name){
        System.out.println(name + "님, 환영합니다!");
    }

    public void printSignUpSuccess(){
        System.out.println("회원가입 되었습니다.");
    }
}
