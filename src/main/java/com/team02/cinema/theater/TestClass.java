package com.team02.cinema.theater;

import com.team02.cinema.theater.domain.Screen;
import com.team02.cinema.theater.service.TheaterService;
import com.team02.cinema.theater.view.TheaterView;

public class TestClass {
    public static void main(String[] args) {
        TheaterService service = new TheaterService();
        TheaterView view = new TheaterView();

        title("1. 상영관 등록 (번호 순서로 정렬)");
        service.addScreen(2, 8, 3);
        service.addScreen(1, 5, 6);
        view.printScreens(service.getScreens());

        title("2. 1관 좌석 배치 (마지막 줄은 프리미엄)");
        Screen screen = service.getScreen(1);
        view.printSeats(screen.getSeats());

        title("3. 좌석 단건 조회");
        view.printSeat(screen.getSeat(0, 0));
        view.printSeat(screen.getSeat(4, 5));

        title("4. 예외 상황");
        expectError("범위 밖 좌석(Z9)", () -> screen.getSeat(25, 8));
        expectError("없는 상영관(9관)", () -> service.getScreen(9));
        expectError("잘못된 좌석 크기", () -> service.addScreen(3, 0, 5));
    }

    private static void title(String text) {
        System.out.println("\n===== " + text + " =====");
    }

    private static void expectError(String situation, Runnable action) {
        try {
            action.run();
            System.out.println("[실패] " + situation + " - 예외가 발생하지 않음");
        } catch (RuntimeException e) {
            System.out.println("[성공] " + situation + " -> " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }
}
