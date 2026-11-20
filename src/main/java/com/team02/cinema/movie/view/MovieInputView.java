package com.team02.cinema.movie.view;

import com.team02.cinema.movie.domain.Movie.AgeRating;
import com.team02.cinema.movie.domain.Movie.Genre;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

// 영화 관련 입력을 받아 알맞은 타입으로 바꿔 주는 클래스
// 변환에 실패하면 IllegalArgumentException을 던지고, Controller에서 잡아 메시지를 출력하도록 함
public class MovieInputView {

    private static final String KEEP_GUIDE = " (그대로 두려면 Enter) : ";

    // Application에서 만든 Scanner를 같이 씀
    private final Scanner scanner;

    public MovieInputView(Scanner scanner) {
        this.scanner = scanner;
    }

    public Long readMovieId() {
        System.out.print("영화 번호를 입력하세요 : ");
        String input = scanner.nextLine().trim();
        try {
            return Long.parseLong(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("영화 번호는 숫자로 입력해야 합니다.");
        }
    }

    // ===== 등록용 입력 =====

    public String readTitle() {
        System.out.print("영화 제목을 입력하세요 : ");
        return scanner.nextLine().trim();
    }

    public Genre readGenre() {
        printGenres();
        System.out.print("장르를 영어로 입력하세요 (예: ACTION) : ");
        return toGenre(scanner.nextLine());
    }

    public int readRunningTime() {
        System.out.print("상영시간(분)을 입력하세요 : ");
        return toRunningTime(scanner.nextLine());
    }

    public AgeRating readAgeRating() {
        printAgeRatings();
        System.out.print("관람등급을 영어로 입력하세요 (예: AGE_15) : ");
        return toAgeRating(scanner.nextLine());
    }

    public LocalDate readReleaseDate() {
        System.out.print("개봉일을 입력하세요 (예: 2024-05-30) : ");
        return toReleaseDate(scanner.nextLine());
    }

    // ===== 수정용 입력 =====
    // 현재 값을 보여주고, 아무것도 입력하지 않으면(Enter) null을 돌려줌
    // MovieService.updateMovie는 null인 항목을 기존 값 그대로 유지함

    public String readTitleForUpdate(String current) {
        System.out.print("영화 제목 [현재: " + current + "]" + KEEP_GUIDE);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return null;
        }
        return input;
    }

    public Genre readGenreForUpdate(Genre current) {
        printGenres();
        System.out.print("장르 [현재: " + current.name() + "]" + KEEP_GUIDE);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return null;
        }
        return toGenre(input);
    }

    // int는 null이 될 수 없어서 Integer를 사용
    public Integer readRunningTimeForUpdate(int current) {
        System.out.print("상영시간(분) [현재: " + current + "]" + KEEP_GUIDE);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return null;
        }
        return toRunningTime(input);
    }

    public AgeRating readAgeRatingForUpdate(AgeRating current) {
        printAgeRatings();
        System.out.print("관람등급 [현재: " + current.name() + "]" + KEEP_GUIDE);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return null;
        }
        return toAgeRating(input);
    }

    public LocalDate readReleaseDateForUpdate(LocalDate current) {
        System.out.print("개봉일 [현재: " + current + "]" + KEEP_GUIDE);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return null;
        }
        return toReleaseDate(input);
    }

    // ===== 목록 출력, 변환 (등록과 수정에서 같이 사용) =====

    private void printGenres() {
        System.out.println("장르 목록:");
        for (Genre genre : Genre.values()) {
            System.out.println(" - " + genre.name() + " (" + genre.getGenre() + ")");
        }
    }

    private void printAgeRatings() {
        System.out.println("관람등급 목록:");
        for (AgeRating ageRating : AgeRating.values()) {
            System.out.println(" - " + ageRating.name() + " (" + ageRating.getRating() + ")");
        }
    }

    private Genre toGenre(String input) {
        String text = input.trim().toUpperCase(); // 소문자로 입력해도 되도록
        try {
            return Genre.valueOf(text);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("목록에 없는 장르입니다: " + text);
        }
    }

    private int toRunningTime(String input) {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("상영시간은 숫자로 입력해야 합니다.");
        }
    }

    private AgeRating toAgeRating(String input) {
        String text = input.trim().toUpperCase();
        try {
            return AgeRating.valueOf(text);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("목록에 없는 관람등급입니다: " + text);
        }
    }

    private LocalDate toReleaseDate(String input) {
        try {
            return LocalDate.parse(input.trim());
        } catch (DateTimeParseException e) {
            // Controller에서 IllegalArgumentException 하나만 잡으면 되도록 바꿔서 던짐
            throw new IllegalArgumentException("개봉일 형식이 잘못되었습니다. 예: 2024-05-30");
        }
    }
}
