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
        Schedule morning = new Schedule(1L, movie, screen, now.plusHours(1), 10000);
        Schedule evening = new Schedule(2L, movie, screen, now.plusHours(7), 10000);
        Member member = new Member("test", "1234", "홍길동", LocalDate.of(2000, 1, 1));

        title("1. 오전 회차 C5, C6 예약");
        Reservation reservation = service.reserve(member, morning, List.of(screen.getSeat(2, 4), screen.getSeat(2, 5)));
        System.out.println("예약번호 : " + reservation.getId() + ", 상태 : " + reservation.getStatus());
        view.printSeats(screen.getSeats(), service.getReservedSeats(morning));
        System.out.println("빈 좌석 수 : " + service.getAvailableSeats(morning).size());

        title("2. 같은 좌석, 저녁 회차는 영향 없음");
        view.printSeats(screen.getSeats(), service.getReservedSeats(evening));
        System.out.println("빈 좌석 수 : " + service.getAvailableSeats(evening).size());

        title("3. 결제 확정 후 취소 - 내역에는 CANCELED로 남고 좌석은 반환");
        service.confirm(reservation.getId());
        System.out.println("확정 후 상태 : " + reservation.getStatus());
        service.cancel(reservation.getId());
        System.out.println("취소 후 상태 : " + reservation.getStatus());
        System.out.println("내 예약 내역 : " + service.getMemberReservations(member).size() + "건 (취소 포함)");
        view.printSeats(screen.getSeats(), service.getReservedSeats(morning));
        System.out.println("빈 좌석 수 : " + service.getAvailableSeats(morning).size());

        title("4. 예약 조회");
        service.reserve(member, evening, List.of(screen.getSeat(1, 0)));
        Member other = new Member("other", "1234", "김철수", LocalDate.of(1999, 1, 1));
        System.out.println("test 회원 예약 : " + service.getMemberReservations(member).size() + "건 (취소 포함)");
        System.out.println("other 회원 예약 : " + service.getMemberReservations(other).size() + "건");
        System.out.println("예약번호 2번 좌석 : " + service.getReservation(2L).getSeats());

        title("5. 예매 규칙");
        System.out.println("4석 예매 : " + service.reserve(member, evening, List.of(screen.getSeat(3, 0), screen.getSeat(3, 1),
                screen.getSeat(3, 2), screen.getSeat(3, 3))).getSeats().size() + "석");

        title("6. 좌석 번호 입력으로 예매");
        Reservation byName = service.reserve(member, morning, "d1, D2 ,e3");
        byName.getSeats().forEach(seat -> System.out.print(seat.getName() + " "));
        System.out.println();
        view.printSeats(screen.getSeats(), service.getReservedSeats(morning));

        title("7. 예외 상황");
        expectError("형식 오류(3A)", () -> service.reserve(member, morning, "3A"));
        expectError("형식 오류(A)", () -> service.reserve(member, morning, "A"));
        expectError("빈 입력", () -> service.reserve(member, morning, "  "));
        expectError("콤마 뒤 빈 값(A5,)", () -> service.reserve(member, morning, "A5,,A6"));
        expectError("범위 밖(A7)", () -> service.reserve(member, morning, "A7"));
        expectError("범위 밖(F1)", () -> service.reserve(member, morning, "F1"));
        service.reserve(member, morning, List.of(screen.getSeat(0, 0)));
        expectError("이미 예약된 좌석(A1) 재예약", () -> service.reserve(member, morning, List.of(screen.getSeat(0, 0))));
        expectError("같은 좌석 중복 선택", () -> service.reserve(member, morning, List.of(screen.getSeat(1, 1), screen.getSeat(1, 1))));
        expectError("다른 상영관 좌석", () -> {
            theater.addScreen(2, 3, 3);
            service.reserve(member, morning, List.of(theater.getScreen(2).getSeat(0, 0)));
        });
        expectError("없는 예약 조회", () -> service.getReservation(99L));
        expectError("취소된 예약 다시 취소", () -> service.cancel(reservation.getId()));
        expectError("취소된 예약 확정", () -> service.confirm(reservation.getId()));
        expectError("없는 예약 취소", () -> service.cancel(99L));
        expectError("5석 선택", () -> service.reserve(member, morning, List.of(screen.getSeat(3, 0), screen.getSeat(3, 1),
                screen.getSeat(3, 2), screen.getSeat(3, 3), screen.getSeat(3, 4))));
        Schedule started = new Schedule(3L, movie, screen, now.minusMinutes(1), 10000);
        expectError("이미 시작한 스케줄 예매", () -> service.reserve(member, started, List.of(screen.getSeat(3, 0))));
        Schedule soon = new Schedule(4L, movie, screen, now.plusMinutes(10), 10000);
        Reservation soonReservation = service.reserve(member, soon, List.of(screen.getSeat(3, 0)));
        expectError("상영 20분 전 이후 취소", () -> service.cancel(soonReservation.getId()));
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
