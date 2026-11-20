package com.team02.cinema.theater.domain;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Screen {
    private static final Pattern SEAT_NAME = Pattern.compile("([A-Za-z])(\\d{1,2})");

    private final int num;
    private final int row;
    private final int col;
    private final List<List<Seat>> seats;

    public Screen(int num, int row, int col){
        if (row < 1 || col < 1) {
            throw new IllegalArgumentException("잘못된 좌석 크기입니다.");
        }
        this.num = num;
        this.row = row;
        this.col = col;
        this.seats = createSeats();
    }

    public List<List<Seat>> createSeats() {
        return IntStream.range(0, row)
                .mapToObj(i -> IntStream.range(0, col).mapToObj(j -> {
                    Seat seat = new Seat(i, j);
                    if(i == row - 1){
                        seat.switchPremium();
                    }

                    return seat;

                }).toList())
                .toList();
    }

    public List<List<Seat>> getSeats() {
        return seats;
    }

    public Seat getSeat(int row, int col) {
        if (row < 0 || row >= this.row || col < 0 || col >= this.col) {
            throw new IllegalArgumentException("존재하지 않는 좌석입니다.");
        }
        return seats.get(row).get(col);
    }

    public Seat getSeat(String name) {
        Matcher matcher = SEAT_NAME.matcher(name == null ? "" : name.trim());
        if (!matcher.matches()) {
            throw new IllegalArgumentException("좌석 번호 형식이 올바르지 않습니다. 예) A3");
        }
        return getSeat(Character.toUpperCase(matcher.group(1).charAt(0)) - 'A', Integer.parseInt(matcher.group(2)) - 1);
    }

    public int getNum() {
        return num;
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
