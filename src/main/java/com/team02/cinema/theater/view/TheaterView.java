package com.team02.cinema.theater.view;

import com.team02.cinema.theater.domain.Seat;
import com.team02.cinema.theater.service.TheaterService;

import java.util.List;

public class TheaterView {
    public void printSeats(List<List<Seat>> seats){
        StringBuilder header = new StringBuilder("  ");
        for(int i = 1; i <= seats.getFirst().size(); i++){
            header.append(" ").append(i).append(" ");
        }

        String screenText = "[ SCREEN ]";
        int screenPos = (header.length() / 2) - (screenText.length() / 2) + 1;
        System.out.println(" ".repeat(screenPos) + screenText);
        System.out.println();
        System.out.println(header);

        for(int i = 0; i < seats.size(); i++){
            StringBuilder line = new StringBuilder((char)(i + 'A') + " ");
            seats.get(i).forEach(seat -> line.append(seat.isReserved() ? "[X]" : "[ ]"));
            System.out.println(line);
        }
    }

    public void printSeat(Seat seat){
        System.out.println(seat);
    }

    public void printScreens(TheaterService theater){
        theater.getScreens().forEach((num, screen) -> System.out.println(num + "관 | " + screen));
    }
}
