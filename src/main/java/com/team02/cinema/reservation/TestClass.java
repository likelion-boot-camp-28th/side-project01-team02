package com.team02.cinema.reservation;

import com.team02.cinema.member.domain.Member;
import com.team02.cinema.movie.domain.Movie.AgeRating;
import com.team02.cinema.movie.domain.Movie.Genre;
import com.team02.cinema.movie.domain.Movie.Movie;
import com.team02.cinema.movie.domain.schedule.Schedule;
import com.team02.cinema.reservation.domain.Reservation;
import com.team02.cinema.reservation.repository.MemoryReservationRepository;
import com.team02.cinema.reservation.service.ReservationService;
import com.team02.cinema.theater.domain.Screen;
import com.team02.cinema.theater.domain.Seat;
import com.team02.cinema.theater.service.TheaterService;
import com.team02.cinema.theater.view.TheaterView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TestClass {
    public static void main(String[] args) {
        TheaterService theater = new TheaterService();
        TheaterView view = new TheaterView();
        ReservationService service = new ReservationService(new MemoryReservationRepository());
        theater.addScreen(1, 5, 6);
        Screen screen = theater.getScreen(1);

        Movie movie = new Movie(1L, "테스트 영화", Genre.ACTION, 120, AgeRating.ALL, LocalDate.now());
        LocalDateTime now = LocalDateTime.now();
        Schedule morning = new Schedule(1L, movie, screen, now, 10000);
        Schedule evening = new Schedule(2L, movie, screen, now.plusHours(6), 10000);
        Member member = new Member("test", "1234", "홍길동", LocalDate.of(2000, 1, 1));

        title("1. 오전 회차 C5, C6 예약");
        Reservation reservation = service.reserve(member, morning, List.of(screen.getSeat(2, 4), screen.getSeat(2, 5)));
        System.out.println("예약번호 : " + reservation.getId());
        view.printSeats(screen.getSeats(), service.getReservedSeats(morning));
        System.out.println("빈 좌석 수 : " + service.getAvailableSeats(morning).size());

        title("2. 같은 좌석, 저녁 회차는 영향 없음");
        view.printSeats(screen.getSeats(), service.getReservedSeats(evening));
        System.out.println("빈 좌석 수 : " + service.getAvailableSeats(evening).size());

        title("3. 예약 취소 후 좌석 반환");
        service.cancel(reservation.getId());
        view.printSeats(screen.getSeats(), service.getReservedSeats(morning));
        System.out.println("빈 좌석 수 : " + service.getAvailableSeats(morning).size());

        title("4. 예외 상황");
        service.reserve(member, morning, List.of(screen.getSeat(0, 0)));
        expectError("이미 예약된 좌석(A1) 재예약", () -> service.reserve(member, morning, List.of(screen.getSeat(0, 0))));
        expectError("같은 좌석 중복 선택", () -> service.reserve(member, morning, List.of(screen.getSeat(1, 1), screen.getSeat(1, 1))));
        expectError("다른 상영관 좌석", () -> {
            theater.addScreen(2, 3, 3);
            service.reserve(member, morning, List.of(theater.getScreen(2).getSeat(0, 0)));
        });
        expectError("없는 예약 취소", () -> service.cancel(99L));
        expectError("회원 null", () -> service.reserve(null, morning, List.of(screen.getSeat(3, 3))));
        expectError("좌석 비어 있음", () -> service.reserve(member, morning, List.of()));
        expectError("좌석 목록 수정", () -> reservation.getSeats().add(new Seat(0, 0)));
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
