package com.team02.cinema.theater.domain;

import java.util.List;
import java.util.stream.IntStream;

public class Screen {
    private final int row;
    private final int col;

    public Screen(int row, int col){
        if (row < 1 || col < 1) {
            throw new IllegalArgumentException("잘못된 좌석 크기입니다.");
        }
        this.row = row;
        this.col = col;
    }

    public List<List<Seat>> createSeats() {
        return IntStream.range(0, row)
                .mapToObj(i -> IntStream.range(0, col).mapToObj(j -> new Seat(i, j)).toList())
                .toList();
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public String toString() {
        return "좌석 수 : " + (row * col);
    }
}
