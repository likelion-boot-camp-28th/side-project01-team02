package com.team02.cinema.reservation;

import com.team02.cinema.member.domain.Member;
import com.team02.cinema.movie.domain.Movie.AgeRating;
import com.team02.cinema.movie.domain.Movie.Genre;
import com.team02.cinema.movie.domain.Movie.Movie;
import com.team02.cinema.movie.domain.schedule.Schedule;
import com.team02.cinema.reservation.domain.Reservation;
import com.team02.cinema.reservation.repository.MemoryReservationRepository;
import com.team02.cinema.reservation.repository.ReservationRepository;
import com.team02.cinema.theater.domain.Seat;
import com.team02.cinema.theater.repository.MemoryScreenRepository;
import com.team02.cinema.theater.service.TheaterService;
import com.team02.cinema.theater.view.TheaterView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TestClass {
    public static void main(String[] args) {
        TheaterService theater = new TheaterService(new MemoryScreenRepository());
        TheaterView view = new TheaterView();
        theater.addScreen(1, 5, 6);

        Movie movie = new Movie(1L, "테스트 영화", Genre.ACTION, 120, AgeRating.ALL, LocalDate.now());
        Schedule schedule = new Schedule(1L, movie, theater.getScreen(1), LocalDateTime.now(), 10000);
        theater.addSchedule(1, schedule);
        Member member = new Member("test", "1234", "홍길동", LocalDate.of(2000, 1, 1));

        title("1. 좌석 선택 후 예약 생성 (C5, C6)");
        List<Seat> seats = List.of(theater.getSeat(1, schedule, 2, 4), theater.getSeat(1, schedule, 2, 5));
        seats.forEach(seat -> seat.reserve(member));
        Reservation reservation = new Reservation(1L, member, schedule, seats);
        printReservation(reservation);
        view.printSeats(theater.getScreenSeats(1, schedule));

        title("2. 저장소 저장 - 아이디 없는 예약에 번호가 붙는지 확인");
        ReservationRepository repository = new MemoryReservationRepository();
        Reservation unsaved = new Reservation(null, member, schedule, seats);
        System.out.println("저장 전 id : " + unsaved.getId());
        Reservation saved1 = repository.save(unsaved);
        Reservation saved2 = repository.save(new Reservation(null, member, schedule, seats));
        System.out.println("저장 후 id : " + saved1.getId() + ", " + saved2.getId());
        System.out.println("전체 조회 : " + repository.findAll().size() + "건");
        System.out.println("id=2 조회 : " + repository.findById(2L).isPresent());
        System.out.println("id=9 조회 : " + repository.findById(9L).isPresent());
        Member other = new Member("other", "1234", "김철수", LocalDate.of(1999, 1, 1));
        System.out.println("test 회원 예약 : " + repository.findByMember(member).size() + "건");
        System.out.println("other 회원 예약 : " + repository.findByMember(other).size() + "건");
        System.out.println("id=1 삭제 : " + repository.deleteById(1L));
        System.out.println("id=1 재삭제 : " + repository.deleteById(1L));
        System.out.println("삭제 후 전체 조회 : " + repository.findAll().size() + "건");
        System.out.println("새 저장 id (번호 재사용 안 됨) : " + repository.save(unsaved).getId());

        title("3. 좌석 목록 수정 불가 확인");
        expectError("getSeats().add()", () -> reservation.getSeats().add(new Seat(0, 0)));

        title("4. 입력 검증");
        expectError("회원 null", () -> new Reservation(1L, null, schedule, seats));
        expectError("스케줄 null", () -> new Reservation(1L, member, null, seats));
        expectError("좌석 null", () -> new Reservation(1L, member, schedule, null));
        expectError("좌석 비어 있음", () -> new Reservation(1L, member, schedule, List.of()));
    }

    private static void printReservation(Reservation reservation) {
        System.out.println("예약번호 : " + reservation.getId());
        System.out.println("회원     : " + reservation.getMember().getLoginId());
        System.out.print("좌석     : ");
        reservation.getSeats().forEach(seat -> System.out.print(seat.getName() + " "));
        System.out.println();
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
