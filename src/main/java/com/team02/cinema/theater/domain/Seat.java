package com.team02.cinema.theater.domain;

public class Seat {
    private final int row;
    private final int col;
    private boolean isPremium;

    public Seat(int row, int col) {
        this.row = row;
        this.col = col;
        this.isPremium = false;
    }

    public boolean isPremium(){return isPremium;}
    public void switchPremium(){isPremium = !isPremium;}

    public String getName() {
        return "" + (char)(row + 'A') + (col + 1);
    }

    @Override
    public String toString() {
        return "Seat{" +
                "name=" + getName() +
                ", premium=" + isPremium +
                '}';
    }
}
