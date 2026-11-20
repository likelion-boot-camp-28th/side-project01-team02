package com.team02.cinema.payment.repository;

public enum PaymentType {

    ////    // 결제 수단
    ///     // 예: "신용 / 체크카드", CREDIT_CARD


    /// 카드 결제
    CREDIT_CARD("신용카드"),
    DEBIT_CARD("체크카드"),
    BANK_TRANSFER("계좌이체");

    private final String methodName;
    PaymentType(String methodName) {this.methodName = methodName; }

    public enum BankType {

        // 은행별 계좌이체
        KB_TRANSFER("KB국민은행"),
        SHINHAN_TRANSFER("신한은행"),
        WOORI_TRANSFER("우리은행"),
        HANA_TRANSFER("하나은행"),
        NH_TRANSFER("NH농협은행"),
        IBK_TRANSFER("IBK기업은행"),
        KAKAO_TRANSFER("카카오뱅크"),
        TOSS_TRANSFER("토스뱅크"),
        K_BANK_TRANSFER("케이뱅크");


        private PaymentType paymentType;
        private final String bankName;

        BankType(String bankName) {
            this.bankName = bankName;
            this.paymentType = null;
        }

        public void SetPayment(PaymentType paymentType) { this.paymentType = paymentType; }
        public PaymentType getPaymentType() { return paymentType; }
        public String getBankName() { return bankName;}

        @Override
        public String toString() {
            return "\n======결제 수단 ======\n"
                     + paymentType + " | " + "은행: " + bankName;
        }


    }
}
