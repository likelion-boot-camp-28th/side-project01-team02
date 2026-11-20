package com.team02.cinema.payment.controller;

import com.team02.cinema.payment.service.PaymentService;

import java.util.Scanner;

public class PaymentController {

    private final PaymentService paymentService;
    private final Scanner sc;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
        this.sc = new Scanner(System.in);

    }

    private int readNonNegativeInt(String prompy) {
        while (true) {
            String input = sc.nextLine().strip();
            try {
                int value = Integer.parseInt(input);
                if (value >= 0) {
                    return value;
                }
            } catch (NumberFormatException e) {

            }
            System.out.println("0 이상의 정수를 입력해 주세요.");
        }
    }




    public void paymentMenu(int memberId, int originalAmount) {

        System.out.println("\n===== 영화 결제 =====");


    }


}
