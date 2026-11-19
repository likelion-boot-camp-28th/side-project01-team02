package com.team02.cinema.theater.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Screen {
    private List<List<Seat>> seats;


    public Screen(int row, int col){
        this.seats = initializeSeats(row, col);
    }

    private List<List<Seat>> initializeSeats(int row, int col) {
        if (row < 1 || col < 1) {
            throw new IllegalArgumentException("잘못된 좌석 크기입니다.");
        }

        List<List<Seat>> seats = new ArrayList<>();
        for (int i = 0; i < row; i++) {
            List<Seat> rows = new ArrayList<>();
            for (int j = 1; j <= col; j++) {
                rows.add(new Seat());
            }
            seats.add(List.copyOf(rows));
        }

        return List.copyOf(seats);
    }

    public void printSeats(){
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("  ");

        for(int i = 1; i <= seats.size(); i++){
            stringBuilder.append(" ").append(i).append(" ");
        }

        String screen = "[ SCREEN ]";
        int screenPos = (stringBuilder.length() / 2) - (screen.length() / 2) + 1;
        System.out.print(" ".repeat(screenPos));
        System.out.println(screen);
        System.out.println();
        System.out.println(stringBuilder);

        IntStream.range(0, seats.size()).forEach(i ->{
            List<Seat> row = seats.get(i);
            System.out.print(i + " ");
            IntStream.range(0, row.size()).forEach(j ->{
                System.out.print("[" + (row.get(j).isReserved() ? 'X' : ' ') + ']');
            });
            System.out.println();
        });
    }
}
