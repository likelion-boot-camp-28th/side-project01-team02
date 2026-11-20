package com.team02.cinema.theater;

import com.team02.cinema.member.domain.Member;
import com.team02.cinema.movie.domain.Movie.AgeRating;
import com.team02.cinema.movie.domain.Movie.Genre;
import com.team02.cinema.movie.domain.Movie.Movie;
import com.team02.cinema.movie.domain.schedule.Schedule;
import com.team02.cinema.theater.service.TheaterService;
import com.team02.cinema.theater.view.TheaterView;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TestClass {
    public static void main(String[] args) {
        TheaterService service = new TheaterService();
        TheaterView view = new TheaterView();

        title("1. 상영관 등록");
        service.addScreen(2, 8, 3);
        service.addScreen(1, 5, 6);
        view.printScreens(service.getScreens());

        Movie movie = new Movie(1L, "테스트 영화", Genre.ACTION, 120, AgeRating.ALL, LocalDate.now());
        LocalDateTime now = LocalDateTime.now();
        Schedule morning = new Schedule(1L, movie, service.getScreen(1), now, 10000);
        Schedule evening = new Schedule(2L, movie, service.getScreen(1), now.plusHours(6), 12000);
        Member member = new Member("test", "1234", "홍길동", LocalDate.of(2000, 1, 1));

        title("2. 예약 전 - 1관 오전 회차");
        view.printSeats(service.getSeats(morning));

        title("3. 오전 회차 A1, B3, E6 예약");
        service.reserveSeat(morning, 0, 0);
        service.reserveSeat(morning, 1, 2);
        service.reserveSeat(morning, 4, 5);
        view.printSeats(service.getSeats(morning));
        System.out.println("빈 좌석 수 : " + service.getAvailableSeats(morning).size());

        title("4. 같은 좌석, 저녁 회차는 영향 없음");
        view.printSeats(service.getSeats(evening));
        System.out.println("빈 좌석 수 : " + service.getAvailableSeats(evening).size());

        title("5. 오전 B3 취소");
        service.cancelSeat(morning, 1, 2);
        view.printSeats(service.getSeats(morning));
        System.out.println("빈 좌석 수 : " + service.getAvailableSeats(morning).size());

        title("6. 좌석 단건 조회");
        view.printSeat(service.getSeat(morning, 0, 0));
        view.printSeat(service.getSeat(morning, 0, 1));

        title("7. 예외 상황");
        expectError("이미 예약된 좌석(A1) 재예약", () -> service.reserveSeat(morning, 0, 0));
        expectError("예약 안 된 좌석(B3) 취소", () -> service.cancelSeat(morning, 1, 2));
        expectError("범위 밖 좌석(Z9)", () -> service.getSeat(morning, 25, 8));
        expectError("없는 상영관(9관)", () -> service.getScreen(9));
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
