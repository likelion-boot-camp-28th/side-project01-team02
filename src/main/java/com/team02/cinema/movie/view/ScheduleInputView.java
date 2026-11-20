package com.team02.cinema.movie.view;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

// 스케줄 관련 입력을 받아 알맞은 타입으로 바꿔 주는 클래스
// 변환에 실패하면 IllegalArgumentException을 던지고, Controller에서 잡아 메시지를 출력하도록 함
public class ScheduleInputView {

    // Application에서 만든 Scanner를 같이 씀
    private final Scanner scanner;

    public ScheduleInputView(Scanner scanner) {
        this.scanner = scanner;
    }

    public Long readScheduleId() {
        System.out.print("스케줄 번호를 입력하세요 : ");
        String input = scanner.nextLine().trim();
        try {
            return Long.parseLong(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("스케줄 번호는 숫자로 입력해야 합니다.");
        }
    }

    public Long readMovieId() {
        System.out.print("상영할 영화 번호를 입력하세요 : ");
        String input = scanner.nextLine().trim();
        try {
            return Long.parseLong(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("영화 번호는 숫자로 입력해야 합니다.");
        }
    }

    public int readScreenNum() {
        System.out.print("상영관 번호를 입력하세요 : ");
        String input = scanner.nextLine().trim();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("상영관 번호는 숫자로 입력해야 합니다.");
        }
    }

    // 날짜와 시각을 따로 입력받아 하나로 합침
    public LocalDateTime readStartTime() {
        System.out.print("상영 날짜를 입력하세요 (예: 2026-11-21) : ");
        String dateInput = scanner.nextLine().trim();
        System.out.print("시작 시각을 입력하세요 (예: 14:30) : ");
        String timeInput = scanner.nextLine().trim();

        try {
            LocalDate date = LocalDate.parse(dateInput);
            LocalTime time = LocalTime.parse(timeInput);
            return date.atTime(time);
        } catch (DateTimeParseException e) {
            // Controller에서 IllegalArgumentException 하나만 잡으면 되도록 바꿔서 던짐
            throw new IllegalArgumentException("날짜 또는 시각 형식이 잘못되었습니다. 예: 2026-11-21, 14:30");
        }
    }

    public int readBasePrice() {
        System.out.print("기본 가격(원)을 입력하세요 : ");
        String input = scanner.nextLine().trim();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("가격은 숫자로 입력해야 합니다.");
        }
    }
}
