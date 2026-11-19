package com.team02.cinema.payment.service;

import com.team02.cinema.payment.domain.PaymentReservation;
import com.team02.cinema.payment.repository.Coupon;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaymentServiceTest {

    private final CouponService coupons = new CouponService();
    private final PointService points = new PointService();
    private final PaymentService payments = new PaymentService(coupons, points);

    @Test
    void appliesCouponAndPointsAndReversesThemOnRefund() {
        Coupon coupon = new Coupon(1001, 1, "할인", 3000, LocalDate.now().plusDays(3));
        coupons.issueCoupon(coupon);
        points.setInitialPoints(1, 5000);
        PaymentReservation reservation = reservationAt(LocalDateTime.now().plusDays(1));

        var payment = payments.pay(reservation, 1, 1001, 1000, "카드");

        assertEquals(20000, payment.getFinalAmount());
        assertEquals(1000, payment.getEarnedPoints());
        assertEquals(5000, points.getBalance(1));
        assertFalse(coupon.isAvailable());
        assertTrue(payments.findMyPayments(2).isEmpty());
        assertThrows(IllegalStateException.class, () -> payments.pay(reservation, 1, null, 0, "현금"));

        payments.refund(payment.getPaymentId(), 1);

        assertTrue(payment.isRefunded());
        assertTrue(coupon.isAvailable());
        assertEquals(5000, points.getBalance(1));
        assertThrows(IllegalStateException.class, () -> payments.refund(payment.getPaymentId(), 1));
    }

    @Test
    void excessivePointRequestLeavesCouponAndPaymentHistoryUnchanged() {
        Coupon coupon = new Coupon(1001, 1, "할인", 3000, LocalDate.now());
        coupons.issueCoupon(coupon);
        points.setInitialPoints(1, 5000);

        assertThrows(IllegalArgumentException.class,
                () -> payments.pay(reservationAt(LocalDateTime.now().plusDays(1)), 1, 1001, 5001, "현금"));

        assertTrue(coupon.isAvailable());
        assertEquals(5000, points.getBalance(1));
        assertTrue(payments.findMyPayments(1).isEmpty());
    }

    @Test
    void rejectsExpiredCouponsAndCouponsOwnedByOtherMembers() {
        coupons.issueCoupon(new Coupon(1001, 1, "할인", 3000, LocalDate.now().minusDays(1)));

        assertThrows(IllegalStateException.class, () -> coupons.calculateDiscount(1, 1001, 24000));
        assertThrows(IllegalArgumentException.class, () -> coupons.calculateDiscount(2, 1001, 24000));
    }

    @Test
    void rejectsRefundAfterCutoff() {
        var payment = payments.pay(reservationAt(LocalDateTime.now().plusMinutes(10)), 1, null, 0, "현금");

        assertThrows(IllegalStateException.class, () -> payments.refund(payment.getPaymentId(), 1));
        assertFalse(payment.isRefunded());
    }

    private PaymentReservation reservationAt(LocalDateTime screeningTime) {
        return new PaymentReservation("R001", 1, "인터스텔라", screeningTime, "3관", List.of("C5", "C6"), 24000);
    }
}
