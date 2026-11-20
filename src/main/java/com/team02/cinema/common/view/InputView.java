package com.team02.cinema.common.view;

import java.time.LocalDate;
import java.util.Scanner;

public class InputView {
    private final Scanner sc;

    public InputView(Scanner sc) {
        this.sc = sc;
    }

    public String readLoginId(){
        System.out.print("아이디를 입력하세요 : ");
        return sc.nextLine();
    }

    public String readPassword(){
        System.out.print("비밀번호를 입력하세요 : ");
        return sc.nextLine();
    }

    public String readName(){
        System.out.print("이름을 입력하세요 : ");
        return sc.nextLine();
    }

    public LocalDate readBirthDate(){
        System.out.print("생년월일을 입력하세요(yyyy-MM-dd) : ");
        return LocalDate.parse(sc.nextLine());
    }

    public String readMenu(){
        System.out.print("번호를 입력하세요 : ");
        return sc.nextLine();
    }
}
