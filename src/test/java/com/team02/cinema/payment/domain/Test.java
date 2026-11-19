package com.team02.cinema.payment.domain;

import com.team02.cinema.payment.service.PointService;

public class Test {

    public static void main(String[] args) {

        PointService pointService = new PointService();

        int memberId = 1;
        String paymentId = "P00000001";

        pointService.setInitPoint(memberId, 5000);

        System.out.println(pointService.getBalance(memberId));
        pointService.usePoint(memberId, 1000, paymentId);
        pointService.showPoint(memberId);



    }
}
