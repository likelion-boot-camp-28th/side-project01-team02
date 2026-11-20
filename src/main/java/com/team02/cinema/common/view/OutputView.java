package com.team02.cinema.common.view;

public class OutputView {
    public void printMenu(){
        System.out.println("1. 회원가입");
        System.out.println("2. 로그인");
        System.out.println("3. 로그아웃");
        System.out.println("4. 내 정보 조회");
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

    public void printMyInfo(String name, int point) {
        System.out.println("이름 : " + name);
        System.out.println("보유 포인트 : " + String.format("%,d", point) + "P");
    }
    public void printWelcome(String name){
        System.out.println(name + "님, 환영합니다!");
    }

    public void printSignUpSuccess(){
        System.out.println("회원가입 되었습니다.");
    }
}
