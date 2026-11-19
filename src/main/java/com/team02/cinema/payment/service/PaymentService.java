package com.team02.cinema.payment.service;

import com.team02.cinema.payment.repository.Payment;


import java.util.LinkedHashMap;

import java.util.Map;

public class PaymentService {

    /// // 결제 처리 및 영수증

    private final Map<String, Payment> payments = new LinkedHashMap<>();
    private final CouponService couponService;
    private final PointService pointService;

    public PaymentService(CouponService couponService, PointService pointService) {
        this.couponService = couponService;
        this.pointService = pointService;
    }




}
