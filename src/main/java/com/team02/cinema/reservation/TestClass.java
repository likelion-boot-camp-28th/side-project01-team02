package com.team02.cinema.reservation;

import com.team02.cinema.member.domain.Member;
import com.team02.cinema.movie.domain.Movie.AgeRating;
import com.team02.cinema.movie.domain.Movie.Genre;
import com.team02.cinema.movie.domain.Movie.Movie;
import com.team02.cinema.movie.domain.schedule.Schedule;
import com.team02.cinema.reservation.controller.ReservationController;
import com.team02.cinema.reservation.domain.Reservation;
import com.team02.cinema.reservation.repository.MemoryReservationRepository;
import com.team02.cinema.reservation.service.ReservationService;
import com.team02.cinema.reservation.view.ReservationInputView;
import com.team02.cinema.reservation.view.ReservationOutputView;
import com.team02.cinema.theater.domain.Screen;
import com.team02.cinema.theater.domain.Seat;
import com.team02.cinema.theater.service.TheaterService;
import com.team02.cinema.theater.view.TheaterView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;

public class TestClass {
    public static void main(String[] args) throws InterruptedException {
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
        service.cancel(member, reservation.getId());
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

        title("6. 결제 실패 - 시스템 취소 (본인 확인, 20분 규칙 없음)");
        Schedule imminent = new Schedule(5L, movie, screen, now.plusMinutes(5), 10000);
        Reservation failed = service.reserve(member, imminent, List.of(screen.getSeat(0, 5)));
        service.cancelBySystem(failed.getId());
        System.out.println("상태 : " + failed.getStatus() + ", 점유 좌석 수 : " + service.getReservedSeats(imminent).size());

        title("6-1. 좌석 번호 입력으로 예매");
        Reservation byName = service.reserve(member, morning, "d1, D2 ,e3");
        byName.getSeats().forEach(seat -> System.out.print(seat.getName() + " "));
        System.out.println();
        view.printSeats(screen.getSeats(), service.getReservedSeats(morning));

        title("6-2. 예매 시각");
        System.out.println("예매 시각 : " + byName.getReservedAt().withNano(0));
        System.out.println("저장 후에도 유지 : " + (service.getReservation(byName.getId()).getReservedAt() == byName.getReservedAt()));

        title("7. 예매 내역 출력");
        ReservationOutputView outputView = new ReservationOutputView();
        outputView.printReservationList(service.getMemberReservations(member));
        System.out.println("오전 회차 유효 예매 있음 : " + service.hasReservation(1L));
        System.out.println("없는 회차 유효 예매 있음 : " + service.hasReservation(99L));

        title("8. 컨트롤러 흐름 (입력 시뮬레이션)");
        Scanner input = new Scanner("a1,a2\nx9\nB1\n1\nabc\n");
        ReservationController controller = new ReservationController(
                service, view, new ReservationInputView(input), outputView);
        controller.reserve(member, evening);
        System.out.println();
        controller.reserve(member, evening);
        System.out.println();
        controller.cancel(member);
        System.out.println();
        controller.cancel(member);

        title("9. 동시 예매 - 같은 좌석에 스레드 50개가 동시에 예매");
        Schedule crowded = new Schedule(6L, movie, screen, now.plusHours(3), 10000);
        raceReserve(service, member, crowded, 50, i -> "A1");
        System.out.println("A1 유효 예매 수 (1이어야 함) : " + service.getReservedSeats(crowded).size());

        title("10. 동시 예매 - 서로 다른 좌석 20개를 동시에 예매");
        Schedule open = new Schedule(7L, movie, screen, now.plusHours(4), 10000);
        raceReserve(service, member, open, 20, i -> "" + (char)('A' + i / 5) + (i % 5 + 1));
        System.out.println("유효 예매 좌석 수 (20이어야 함) : " + service.getReservedSeats(open).size());

        title("11. 동시 취소 - 같은 예매를 스레드 20개가 동시에 취소");
        Reservation target = service.reserve(member, open, "E1");
        race(20, i -> service.cancelBySystem(target.getId()));
        System.out.println("최종 상태 (CANCELED여야 함) : " + target.getStatus());

        title("12. 동시 예매 + 취소 - 취소된 좌석을 두고 스레드 20개가 경쟁");
        Reservation freed = service.reserve(member, open, "E2");
        service.cancelBySystem(freed.getId());
        race(20, i -> service.reserve(member, open, "E2"));
        System.out.println("E2 유효 예매 수 (1이어야 함) : " + service.getReservedSeats(open).stream()
                .filter(seat -> seat.getName().equals("E2")).count());

        title("13. 여러 회차 동시 예매 - 회차 5개에 같은 좌석을 각각 10명씩");
        List<Schedule> schedules = java.util.stream.IntStream.range(0, 5)
                .mapToObj(i -> new Schedule(10L + i, movie, screen, now.plusHours(10 + i), 10000)).toList();
        race(50, i -> service.reserve(member, schedules.get(i % 5), "B2"));
        schedules.forEach(sch -> System.out.println(sch.getId() + "번 회차 B2 점유 : " + service.getReservedSeats(sch).size()));

        title("14. 예외 상황");
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
        expectError("취소된 예약 다시 취소", () -> service.cancel(member, reservation.getId()));
        expectError("취소된 예약 확정", () -> service.confirm(reservation.getId()));
        expectError("시스템 취소 - 이미 취소된 예약", () -> service.cancelBySystem(failed.getId()));
        expectError("없는 예약 취소", () -> service.cancel(member, 99L));
        expectError("5석 선택", () -> service.reserve(member, morning, List.of(screen.getSeat(3, 0), screen.getSeat(3, 1),
                screen.getSeat(3, 2), screen.getSeat(3, 3), screen.getSeat(3, 4))));
        Schedule started = new Schedule(3L, movie, screen, now.minusMinutes(1), 10000);
        expectError("이미 시작한 스케줄 예매", () -> service.reserve(member, started, List.of(screen.getSeat(3, 0))));
        Schedule soon = new Schedule(4L, movie, screen, now.plusMinutes(10), 10000);
        Reservation soonReservation = service.reserve(member, soon, List.of(screen.getSeat(3, 0)));
        expectError("다른 회원이 취소", () -> service.cancel(other, soonReservation.getId()));
        expectError("상영 20분 전 이후 취소", () -> service.cancel(member, soonReservation.getId()));
        expectError("회원 null", () -> service.reserve(null, morning, List.of(screen.getSeat(3, 3))));
        expectError("좌석 비어 있음", () -> service.reserve(member, morning, List.of()));
        expectError("좌석 목록 수정", () -> reservation.getSeats().add(new Seat(0, 0)));
    }

    private static void raceReserve(ReservationService service, Member member, Schedule schedule,
                                    int threads, IntFunction<String> seatName) throws InterruptedException {
        race(threads, i -> service.reserve(member, schedule, seatName.apply(i)));
    }

    private static void race(int threads, IntConsumer action) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch ready = new CountDownLatch(threads);
        CountDownLatch start = new CountDownLatch(1);
        AtomicInteger success = new AtomicInteger();
        AtomicInteger fail = new AtomicInteger();
        for (int i = 0; i < threads; i++) {
            int index = i;
            executor.submit(() -> {
                ready.countDown();
                try {
                    start.await();
                    action.accept(index);
                    success.incrementAndGet();
                } catch (RuntimeException | InterruptedException e) {
                    fail.incrementAndGet();
                }
            });
        }
        ready.await();
        start.countDown();
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
        System.out.println("성공 " + success.get() + "건, 실패 " + fail.get() + "건");
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
