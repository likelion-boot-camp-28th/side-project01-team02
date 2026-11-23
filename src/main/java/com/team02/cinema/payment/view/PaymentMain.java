package com.team02.cinema.payment.view;

import com.team02.cinema.payment.controller.CouponController;
import com.team02.cinema.payment.controller.PaymentController;
import com.team02.cinema.payment.controller.PointController;
import com.team02.cinema.payment.domain.PaymentMenu;
import com.team02.cinema.payment.service.CouponService;
import com.team02.cinema.payment.service.PaymentService;
import com.team02.cinema.payment.service.PointService;

import java.util.Scanner;

public class PaymentMain {

    Scanner sc = new Scanner(System.in);

    CouponService couponService = new CouponService();
    PointService pointService = new PointService();
    PaymentService paymentService = new PaymentService(couponService, pointService);

    CouponController couponController = new CouponController(couponService);
    PointController pointController = new PointController(pointService);
    PaymentController paymentController = new PaymentController(paymentService, couponController, pointController);

    PaymentMenu paymentMenu = new PaymentMenu(paymentController, couponController, pointController);



}
