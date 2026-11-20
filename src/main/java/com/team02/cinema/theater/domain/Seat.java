package com.team02.cinema.theater.domain;

public class Seat {
    private final int row;
    private final int col;
    private boolean reserved;
    private boolean isPremium;

    public Seat(int row, int col) {
        this.row = row;
        this.col = col;
        this.isPremium = false;
    }

    public void reserve() {
        if (reserved) {
            throw new IllegalStateException("이미 예약된 좌석입니다.");
        }
        reserved = true;
    }

    public void cancel() {
        if (!reserved) {
            throw new IllegalStateException("예약되지 않은 좌석입니다.");
        }
        reserved = false;
    }

    public boolean isReserved(){return reserved;}
    public boolean isPremium(){return isPremium;}
    public void switchPremium(){isPremium = !isPremium;}

    public String getName() {
        return "" + (char)(row + 'A') + (col + 1);
    }

    @Override
    public String toString() {
        return "Seat{" +
                "name=" + getName() +
                ", reserved=" + reserved +
                '}';
    }
}
