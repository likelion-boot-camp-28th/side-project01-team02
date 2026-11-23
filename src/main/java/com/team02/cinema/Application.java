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
        memberRepository.save(new Member("admin", "admin1234", "관리자", LocalDate.of(1990,1,1), Role.ADMIN));
        MemberService memberService = new MemberService(memberRepository);
        OutputView outputView = new OutputView();
            MemberController memberController = new MemberController(memberService,outputView);
        InputView inputView = new InputView(scanner);

        MovieRepository movieRepository = new MemoryMovieRepository();
        ScheduleRepository scheduleRepository = new MemoryScheduleRepository();
        MovieService movieService = new MovieService(movieRepository, scheduleRepository);
        ReservationService reservationService = new ReservationService(new MemoryReservationRepository());
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
        ReservationController reservationController = new ReservationController(reservationService,new TheaterView() ,
                new ReservationInputView(scanner), new ReservationOutputView());
        CouponService couponService = new CouponService();
        PointService pointService = new PointService();
        PaymentService paymentService = new PaymentService(couponService, pointService, reservationService);
        CouponController couponController = new CouponController(couponService);
        PointController pointController = new PointController(pointService);
        PaymentController paymentController = new PaymentController(paymentService, couponController, pointController);
        PaymentMenu paymentMenu = new PaymentMenu(paymentController, couponController, pointController);

        // ===== 초기 데이터 =====
        new MovieDataInitializer(movieService, scheduleService, theaterService).init();



        boolean running = true;
        while(running){
            outputView.printMenu();
            String choice = inputView.readMenu();

            switch (choice){
                case "0" -> {
                    outputView.printExit();
                    running = false;
                }
                case "1" -> {
                    String id = inputView.readLoginId();
                    String password = inputView.readPassword();
                    String name = inputView.readName();
                    try {
                        LocalDate birth = inputView.readBirthDate();
                        memberController.signUp(id,password,name,birth);
                    } catch (DateTimeParseException e) {
                        outputView.printError(ErrorMessage.INVALID_BIRTH_DATE.getMessage());
                    }
                }
                case "2" -> {
                    String id = inputView.readLoginId();
                    String password = inputView.readPassword();
                    memberController.login(id,password);
                }
                case "3" -> {
                    if (memberController.getLoginMember().isEmpty()){
                        outputView.printError(ErrorMessage.NOT_LOGGED_IN.getMessage());
                    }else{
                        memberController.logout();
                        outputView.printLogout();
                    }
                }
                case "4" -> {
                    Member member = findLoginMember(memberController,outputView);
                    if (member != null){
                        outputView.printMyInfo(member.getName(), member.getPoint());
                    }
                }
                case "5" -> {
                    if (memberController.getLoginMember().isEmpty()) {
                        outputView.printError(ErrorMessage.NOT_LOGGED_IN.getMessage());
                    }else if(!memberController.canEnterAdminMenu()) {
                        outputView.printError(ErrorMessage.NO_ADMIN_PERMISSION.getMessage());
                    }else {
                        runAdminMenu(inputView, outputView, movieController, scheduleController);                    }
                }
                case "6" -> movieController.showMovies();
                case "7" -> movieController.showMovieDetail();
                case "8" -> {
                    Member member = findLoginMember(memberController,outputView);
                    if (member != null){
                        reservationController.showMyReservations(member);
                    }
                }
                case "9" -> {
                    Member member = findLoginMember(memberController,outputView);
                    if (member != null){
                        reservationController.cancel(member);
                    }
                }
                case "10" -> {
                    Member member = findLoginMember(memberController, outputView);
                    if (member != null) {
                        paymentMenu.runMemberId(member, scanner, null);
                    }
                }
                case "11" -> {
                    Member member = findLoginMember(memberController, outputView);
                    if (member != null) {
                        Optional<Long> movieId = movieController.showMovieDetail();
                        if (movieId.isPresent()) {
                            try {
                                Long scheduleId = inputView.readScheduleId();
                                // 고른 영화의 상영인지 확인 (다른 영화의 상영 번호는 예매되지 않도록)
                                Schedule schedule = scheduleService.findScheduleOfMovie(movieId.get(), scheduleId);
                                Reservation reservation = reservationController.reserve(member, schedule);
                                if (reservation != null) {
                                    paymentMenu.runMemberId(member, scanner, reservation);
                                }
                            } catch (IllegalArgumentException e) {
                                outputView.printError(e.getMessage());
                            }
                        }
                    }
                }
                default -> outputView.printError(ErrorMessage.INVALID_MENU.getMessage());
            }

        }
    }
    private static void runAdminMenu(InputView inputView, OutputView outputView,
                                     MovieController movieController, ScheduleController scheduleController) {
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
                case "0" -> inAdmin = false;
                default -> outputView.printError(ErrorMessage.INVALID_MENU.getMessage());
            }
        }
    }

    private static Member findLoginMember(MemberController memberController, OutputView outputView){
        if (memberController.getLoginMember().isEmpty()){
            outputView.printError(ErrorMessage.NOT_LOGGED_IN.getMessage());
            return null;
        }
        return memberController.getLoginMember().get();
    }
}
