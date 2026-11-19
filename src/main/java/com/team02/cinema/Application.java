package com.team02.cinema;

import com.team02.cinema.common.InputView;
import com.team02.cinema.common.OutputView;

public class Application {

    public static void main(String[] args) {
        OutputView outputView = new OutputView();
        InputView inputView = new InputView();

        // TODO(A): 메뉴 컨트롤러 연결, TODO(B): 초기 더미 데이터 등록
        outputView.printLine("영화 예매 시스템을 시작합니다.");
        inputView.close();
    }
}
