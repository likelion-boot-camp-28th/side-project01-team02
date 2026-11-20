package com.team02.cinema.movie;

import com.team02.cinema.movie.controller.MovieController;
import com.team02.cinema.movie.controller.ScheduleController;
import com.team02.cinema.movie.initial.MovieDataInitializer;
import com.team02.cinema.movie.repository.MemoryMovieRepository;
import com.team02.cinema.movie.repository.MemoryScheduleRepository;
import com.team02.cinema.movie.repository.MovieRepository;
import com.team02.cinema.movie.repository.ScheduleRepository;
import com.team02.cinema.movie.service.MovieService;
import com.team02.cinema.movie.service.ScheduleService;
import com.team02.cinema.movie.view.MovieInputView;
import com.team02.cinema.movie.view.MovieOutputView;
import com.team02.cinema.movie.view.ScheduleInputView;
import com.team02.cinema.movie.view.ScheduleOutputView;
import com.team02.cinema.reservation.repository.MemoryReservationRepository;
import com.team02.cinema.reservation.service.ReservationService;
import com.team02.cinema.theater.service.TheaterService;

import java.util.Scanner;

// 실제 사용자처럼 메뉴를 골라 가며 movie 패키지 기능을 직접 확인하는 테스트
// 프로젝트 루트(Side Project)를 작업 디렉터리로 실행해야 data/*.csv를 찾을 수 있음
public class TestClass {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in); // 모든 입력 화면이 이 Scanner 하나를 같이 씀

        MovieRepository movieRepository = new MemoryMovieRepository();
        ScheduleRepository scheduleRepository = new MemoryScheduleRepository();
        MovieService movieService = new MovieService(movieRepository, scheduleRepository);
        // 예매 쪽 ReservationChecker 구현 전이라 "예매 없음"으로 고정
        ScheduleService scheduleService = new ScheduleService(scheduleRepository, movieRepository,
                scheduleId -> false);
        ReservationService reservationService = new ReservationService(new MemoryReservationRepository());

        TheaterService theaterService = new TheaterService();
        theaterService.addScreen(1, 5, 5); // 1관: 5행 x 5열

        MovieOutputView movieOutputView = new MovieOutputView();
        ScheduleOutputView scheduleOutputView = new ScheduleOutputView();
        MovieController movieController = new MovieController(movieService, scheduleService, reservationService,
                new MovieInputView(scanner), movieOutputView, scheduleOutputView);
        ScheduleController scheduleController = new ScheduleController(scheduleService, movieService, theaterService,
                new ScheduleInputView(scanner), scheduleOutputView, movieOutputView);

        // ===== 초기 데이터 =====
        new MovieDataInitializer(movieService, scheduleService, theaterService).init();

        // ===== 메인 메뉴 =====
        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("===== 영화관 =====");
            System.out.println("1. 영화 목록");
            System.out.println("2. 영화 상세 (상영 스케줄, 잔여 좌석)");
            System.out.println("3. 관리자 메뉴");
            System.out.println("0. 종료");
            System.out.print("번호를 입력하세요 : ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> movieController.showMovies();
                case "2" -> movieController.showMovieDetail();
                case "3" -> runAdminMenu(scanner, movieController, scheduleController);
                case "0" -> {
                    System.out.println("프로그램을 종료합니다.");
                    running = false;
                }
                default -> System.out.println("잘못 입력하셨습니다.");
            }
        }
    }

    // 관리자 메뉴: 0을 누르면 메인 메뉴로 돌아감
    private static void runAdminMenu(Scanner scanner, MovieController movieController,
                                     ScheduleController scheduleController) {
        boolean inAdmin = true;
        while (inAdmin) {
            System.out.println();
            System.out.println("===== 관리자 메뉴 =====");
            System.out.println("1. 영화 등록");
            System.out.println("2. 영화 수정");
            System.out.println("3. 영화 삭제");
            System.out.println("4. 스케줄 목록");
            System.out.println("5. 스케줄 등록");
            System.out.println("6. 스케줄 삭제");
            System.out.println("0. 이전 메뉴");
            System.out.print("번호를 입력하세요 : ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> movieController.registerMovie();
                case "2" -> movieController.updateMovie();
                case "3" -> movieController.deleteMovie();
                case "4" -> scheduleController.showSchedules();
                case "5" -> scheduleController.registerSchedule();
                case "6" -> scheduleController.deleteSchedule();
                case "0" -> inAdmin = false;
                default -> System.out.println("잘못 입력하셨습니다.");
            }
        }
    }
}
