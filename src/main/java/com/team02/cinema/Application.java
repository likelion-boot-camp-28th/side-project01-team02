package com.team02.cinema;

import com.team02.cinema.common.exception.ErrorMessage;
import com.team02.cinema.common.view.InputView;
import com.team02.cinema.common.view.OutputView;
import com.team02.cinema.member.controller.MemberController;
import com.team02.cinema.member.domain.Member;
import com.team02.cinema.member.domain.Role;
import com.team02.cinema.member.repository.MemberRepository;
import com.team02.cinema.member.repository.MemoryMemberRepository;
import com.team02.cinema.member.service.MemberService;
import com.team02.cinema.movie.controller.MovieController;
import com.team02.cinema.movie.controller.ScheduleController;
import com.team02.cinema.movie.domain.schedule.Schedule;
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
import com.team02.cinema.payment.controller.CouponController;
import com.team02.cinema.payment.controller.PaymentController;
import com.team02.cinema.payment.controller.PointController;
import com.team02.cinema.payment.domain.PaymentMenu;
import com.team02.cinema.payment.service.CouponService;
import com.team02.cinema.payment.service.PaymentService;
import com.team02.cinema.payment.service.PointService;
import com.team02.cinema.reservation.controller.ReservationController;
import com.team02.cinema.reservation.domain.Reservation;
import com.team02.cinema.reservation.domain.ReservationStatus;
import com.team02.cinema.reservation.repository.MemoryReservationRepository;
import com.team02.cinema.reservation.service.ReservationService;
import com.team02.cinema.reservation.view.ReservationInputView;
import com.team02.cinema.reservation.view.ReservationOutputView;
import com.team02.cinema.theater.service.TheaterService;
import com.team02.cinema.theater.view.TheaterView;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.Scanner;


public class Application {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MemberRepository memberRepository = new MemoryMemberRepository();
        memberRepository.save(new Member("admin", "admin1234", "관리자", LocalDate.of(1990, 1, 1), Role.ADMIN));
        ReservationService reservationService = new ReservationService(new MemoryReservationRepository());
        MemberService memberService = new MemberService(memberRepository, reservationService::hasUpcomingConfirmed);
        OutputView outputView = new OutputView();
        MemberController memberController = new MemberController(memberService, outputView);
        InputView inputView = new InputView(scanner);

        MovieRepository movieRepository = new MemoryMovieRepository();
        ScheduleRepository scheduleRepository = new MemoryScheduleRepository();
        MovieService movieService = new MovieService(movieRepository, scheduleRepository);
        ScheduleService scheduleService = new ScheduleService(scheduleRepository, movieRepository,
                reservationService::hasReservation);

        TheaterService theaterService = new TheaterService();
        theaterService.addScreen(1, 5, 5); // 1관: 5행 x 5열

        MovieOutputView movieOutputView = new MovieOutputView();
        ScheduleOutputView scheduleOutputView = new ScheduleOutputView();
        MovieController movieController = new MovieController(movieService, scheduleService, reservationService,
                new MovieInputView(scanner), movieOutputView, scheduleOutputView);
        ScheduleController scheduleController = new ScheduleController(scheduleService, movieService, theaterService,
                new ScheduleInputView(scanner), scheduleOutputView, movieOutputView);
        CouponService couponService = new CouponService();
        PointService pointService = new PointService();
        PaymentService paymentService = new PaymentService(couponService, pointService, reservationService);
        ReservationController reservationController = new ReservationController(reservationService, paymentService,
                new TheaterView(), new ReservationInputView(scanner), new ReservationOutputView());
        CouponController couponController = new CouponController(couponService);
        PointController pointController = new PointController(pointService);
        PaymentController paymentController = new PaymentController(paymentService, couponController, pointController);
        PaymentMenu paymentMenu = new PaymentMenu(paymentController, couponController, pointController);

        // ===== 초기 데이터 =====
        new MovieDataInitializer(movieService, scheduleService, theaterService).init();


        boolean running = true;
        while (running) {
            if (memberController.getLoginMember().isEmpty()) {
                running = runGuestMenu(inputView, outputView, memberController, movieController);
            } else if (memberController.canEnterAdminMenu()) {
                runAdminMenu(inputView, outputView, movieController, scheduleController, memberController);
            } else {
                runMemberMenu(inputView, outputView, scanner, memberController, movieController, scheduleService, reservationService, reservationController, paymentMenu);
            }
        }
    }

    private static void runAdminMenu(InputView inputView, OutputView outputView,
                                     MovieController movieController, ScheduleController scheduleController, MemberController memberController) {
        boolean inAdmin = true;
        while (inAdmin) {
            outputView.printAdminMenu();
            String choice = inputView.readMenu();
            switch (choice) {
                case "1" -> movieController.registerMovie();
                case "2" -> movieController.updateMovie();
                case "3" -> movieController.deleteMovie();
                case "4" -> scheduleController.showSchedules();
                case "5" -> scheduleController.registerSchedule();
                case "6" -> scheduleController.deleteSchedule();
                case "0" -> {
                    inAdmin = false;
                    memberController.logout();
                    outputView.printLogout();
                }
                default -> outputView.printError(ErrorMessage.INVALID_MENU.getMessage());
            }
        }
    }

    private static boolean runGuestMenu(InputView inputView, OutputView outputView, MemberController memberController, MovieController movieController) {
        outputView.printGuestMenu();
        String choice = inputView.readMenu();
        switch (choice) {
            case "1" -> {
                String id = inputView.readLoginId();
                String password = inputView.readPassword();
                String name = inputView.readName();
                try {
                    LocalDate birth = inputView.readBirthDate();
                    memberController.signUp(id, password, name, birth);
                } catch (DateTimeParseException e) {
                    outputView.printError(ErrorMessage.INVALID_BIRTH_DATE.getMessage());
                }
            }
            case "2" -> {
                String id = inputView.readLoginId();
                String password = inputView.readPassword();
                memberController.login(id, password);
            }
            case "3" -> {
                movieController.showMovies();
            }
            case "4" -> {
                movieController.showMovieDetail();
            }
            case "0" -> {
                outputView.printExit();
                return false;
            }
            default -> outputView.printError(ErrorMessage.INVALID_MENU.getMessage());
        }
        return true;
    }

    private static void runMemberMenu(InputView inputView, OutputView outputView, Scanner scanner,
                                      MemberController memberController, MovieController movieController,
                                      ScheduleService scheduleService, ReservationService reservationService,
                                      ReservationController reservationController, PaymentMenu paymentMenu) {
        Member member = memberController.getLoginMember().get();
        outputView.printMemberMenu();
        String choice = inputView.readMenu();
        switch (choice) {
            case "1" -> movieController.showMovies();
            case "2" -> movieController.showMovieDetail();
            case "3" -> {
                Optional<Long> movieId = movieController.showMovieDetail();
                if (movieId.isPresent()) {
                    try {
                        Long scheduleId = inputView.readScheduleId();
                        // 고른 영화의 상영인지 확인 (다른 영화의 상영 번호는 예매되지 않도록)
                        Schedule schedule = scheduleService.findScheduleOfMovie(movieId.get(), scheduleId);
                        Reservation reservation = reservationController.reserve(member, schedule);
                        if (reservation != null) {
                            paymentMenu.runMemberId(member, scanner, reservation);
                            if (reservation.getStatus() == ReservationStatus.PENDING) {
                                reservationService.cancelBySystem(reservation.getId());
                            }
                        }
                    } catch (IllegalArgumentException e) {
                        outputView.printError(e.getMessage());
                    }
                }
            }
            case "4" -> reservationController.showMyReservations(member);
            case "5" -> {
                reservationController.cancel(member);
            }
            case "6" -> {
                paymentMenu.runMemberId(member, scanner, null);
            }
            case "7" -> {
                outputView.printMyInfo(member.getName(), member.getPoint());
            }
            case "8" -> {
                memberController.withdraw();
            }
            case "0" -> {
                memberController.logout();
                outputView.printLogout();
            }
            default -> outputView.printError(ErrorMessage.INVALID_MENU.getMessage());
        }
    }
}
