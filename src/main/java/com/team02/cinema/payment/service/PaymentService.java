package com.team02.cinema.payment.service;

import com.team02.cinema.payment.repository.Payment;


import javax.swing.plaf.synth.SynthRadioButtonMenuItemUI;
import java.util.HashMap;
import java.util.LinkedHashMap;

import java.util.Map;

public class PaymentService {

    /// // 결제 처리 및 영수증
    private final CouponService couponService;
    private final PointService pointService;

    public PaymentService(CouponService couponService, PointService pointService) {
        this.couponService = couponService;
        this.pointService = pointService;
    }


    /// 결제가 완료된 Payment 객체들을 저장
    ///
    /// Key   : 결제번호
    /// Value : 결제 객체
    private final Map<String, Payment> payments = new HashMap<>();


    /// 결제번호 생성에 사용하는 일련번호
    /// 결제가 생성될 때마다 1씩 증가
    private int nextPaymentNumber = 1;
    String payNumer = "P0000000" + nextPaymentNumber;


    /// 결제 진행







}
