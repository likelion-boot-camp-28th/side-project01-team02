package com.team02.cinema.payment.view;

import com.team02.cinema.member.domain.Member;
import com.team02.cinema.movie.domain.Movie.AgeRating;
import com.team02.cinema.movie.domain.Movie.Genre;
import com.team02.cinema.movie.domain.Movie.Movie;
import com.team02.cinema.movie.domain.schedule.Schedule;
import com.team02.cinema.payment.controller.CouponController;
import com.team02.cinema.payment.controller.PaymentController;
import com.team02.cinema.payment.controller.PointController;
import com.team02.cinema.payment.domain.PaymentMenu;
import com.team02.cinema.payment.service.CouponService;
import com.team02.cinema.payment.service.PaymentService;
import com.team02.cinema.payment.service.PointService;
import com.team02.cinema.reservation.domain.Reservation;
import com.team02.cinema.reservation.repository.MemoryReservationRepository;
import com.team02.cinema.reservation.service.ReservationService;
import com.team02.cinema.theater.domain.Screen;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class PaymentTestMain {

    public static void main(String[] args) {

        long testMemberId = 1;
        Scanner sc = new Scanner(System.in);
        CouponService couponService = new CouponService();
        PointService pointService = new PointService();
        PaymentService paymentService = new PaymentService(couponService, pointService);

        CouponController couponController = new CouponController(couponService);
        PointController pointController = new PointController(pointService);
        PaymentController paymentController = new PaymentController(paymentService, couponController, pointController);
        PaymentMenu paymentMenu = new PaymentMenu(paymentController, couponController, pointController);

        Member member = new Member("test", "1234", "테스트 회원", LocalDate.of(2000, 1, 1));
        member.assignId(1L);
        Screen screen = new Screen(1, 5, 6);
        Movie movie = new Movie(1L, "테스트 영화", Genre.ACTION, 120, AgeRating.ALL, LocalDate.now());
        Schedule schedule = new Schedule(1L, movie, screen, LocalDateTime.now().plusHours(1), 30_000);

        ReservationService reservationService = new ReservationService(new MemoryReservationRepository());

        // 좌석 인덱스는 0부터 시작하므로 (2, 4)는 C5, (2, 5)는 C6이다.
        Reservation reservation = reservationService.reserve(member, schedule, List.of(screen.getSeat(2, 4), screen.getSeat(2, 5)));
        couponController.issueWelcome3000Coupon(member.getId());
        paymentMenu.run(member.getId(), sc, reservation);

    }
}
