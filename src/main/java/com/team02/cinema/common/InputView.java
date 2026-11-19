package com.team02.cinema.common;

import java.util.Scanner;

/**
 * 모든 콘솔 입력은 이 클래스를 거친다.
 */
public class InputView {

    private final Scanner scanner = new Scanner(System.in);

    public String readLine(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            return "";
        }
        return scanner.nextLine().trim();
    }

    public void close() {
        scanner.close();
    }
}
