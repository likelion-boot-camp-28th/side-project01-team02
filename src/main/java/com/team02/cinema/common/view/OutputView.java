package com.team02.cinema.common.view;

import com.team02.cinema.movie.controller.MovieController;
import com.team02.cinema.movie.controller.ScheduleController;

public class OutputView {
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

    public void printAdminMenu(){
        System.out.println("1. 영화 등록");
        System.out.println("2. 영화 수정");
        System.out.println("3. 영화 삭제");
        System.out.println("4. 스케줄 목록");
        System.out.println("5. 스케줄 등록");
        System.out.println("6. 스케줄 삭제");
        System.out.println("0. 로그아웃");
    }

    public void printWithdraw(){
        System.out.println("회원 탈퇴가 완료되었습니다.");
    }

    public void printMemberMenu(){
        System.out.println("===== 회원 메뉴 =====");
        System.out.println("1. 영화 목록");
        System.out.println("2. 영화 상세");
        System.out.println("3. 예매하기");
        System.out.println("4. 예매 내역");
        System.out.println("5. 예매 취소");
        System.out.println("6. 결제·쿠폰·포인트");
        System.out.println("7. 내 정보 조회");
        System.out.println("8. 회원 탈퇴");
        System.out.println("0. 로그아웃");
    }

    public void printGuestMenu(){
        System.out.println("===== 비회원 메뉴 =====");
        System.out.println("1. 회원가입");
        System.out.println("2. 로그인");
        System.out.println("3. 영화 목록");
        System.out.println("4. 영화 상세");
        System.out.println("0. 종료");
    }
}
