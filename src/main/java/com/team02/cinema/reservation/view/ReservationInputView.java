package com.team02.cinema.reservation.view;

import java.util.Scanner;

public class ReservationInputView {
    private final Scanner scanner;

    public ReservationInputView(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readSeatNames() {
        System.out.print("좌석 번호를 입력하세요 (예: A3, A4) : ");
        return scanner.nextLine();
    }

    public Long readReservationId() {
        System.out.print("예매 번호를 입력하세요 : ");
        try {
            return Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("예매 번호는 숫자로 입력해야 합니다.");
        }
    }
}
