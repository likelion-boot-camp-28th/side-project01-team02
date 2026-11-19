package com.team02.cinema.theater.domain;

import com.team02.cinema.member.domain.Member;

public class Seat {
    private final int row;
    private final int col;
    private Member member;

    public Seat(int row, int col) {
        this.row = row;
        this.col = col;
        member = null;
    }

    public void reserve(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("회원은 필수입니다.");
        }
        if (isReserved()) {
            throw new IllegalStateException("이미 예약된 좌석입니다.");
        }
        this.member = member;
    }

    public void cancel() {
        if (!isReserved()) {
            throw new IllegalStateException("예약되지 않은 좌석입니다.");
        }
        member = null;
    }

    public Member getMember() {
        return member;
    }

    public boolean isReserved(){return member != null;}

    public String getName() {
        return "" + (char)(row + 'A') + (col + 1);
    }

    @Override
    public String toString() {
        return "Seat{" +
                "name=" + getName() +
                ", member=" + member +
                '}';
    }
}
