package com.team02.cinema.member.domain;

import java.time.LocalDate;

public class Member {
    private long id;
    private String loginId;
    private String password;
    private String name;
    private LocalDate birthDate;
    private Role role;
    private int point;

    public Member(String loginId, String password, String name, LocalDate birthDate) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.birthDate = birthDate;
        this.role = Role.USER;
        this.point = 0;
    }

    public void assignId(long id){
        this.id = id;
    }

    public String getLoginId(){
        return loginId;
    }

    public boolean matchesPassword(String password) {
        return this.password.equals(password);
    }

    public boolean isAdmin(){
        return role == Role.ADMIN;
    }
}
