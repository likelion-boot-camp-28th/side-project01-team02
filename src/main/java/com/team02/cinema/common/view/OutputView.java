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

    public void printInvalidBirthDate(){
        System.out.println("생년월일 형식이 올바르지 않습니다. (예 : 2000-01-15)");
    }

    public void printNotLoggedIn(){
        System.out.println("로그인 상태가 아닙니다.");
    }

    public void printLogout(){
        System.out.println("로그아웃되었습니다.");
    }

    public void printInvalidMenu(){
        System.out.println("잘못입력하셨습니다.");
    }
}
