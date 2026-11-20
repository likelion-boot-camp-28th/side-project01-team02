package com.team02.cinema.movie.controller;

import com.team02.cinema.movie.domain.Movie.AgeRating;
import com.team02.cinema.movie.domain.Movie.Genre;
import com.team02.cinema.movie.domain.Movie.Movie;
import com.team02.cinema.movie.service.MovieService;
import com.team02.cinema.movie.view.MovieInputView;
import com.team02.cinema.movie.view.MovieOutputView;

import java.time.LocalDate;
import java.util.List;

// 영화 메뉴 기능을 담당 (입력 -> 서비스 호출 -> 결과 출력)
// 예외는 여기서 잡아 메시지만 출력하고, 프로그램은 계속 실행되도록 함
public class MovieController {

    private final MovieService movieService;
    private final MovieInputView inputView;
    private final MovieOutputView outputView;

    public MovieController(MovieService movieService, MovieInputView inputView, MovieOutputView outputView) {
        this.movieService = movieService;
        this.inputView = inputView;
        this.outputView = outputView;
    }

    // 영화 목록 조회
    public void showMovies() {
        List<Movie> movies = movieService.findAllMovies();
        outputView.printMovieList(movies);
    }

    // 영화 등록 (관리자)
    public void registerMovie() {
        try {
            String title = inputView.readTitle();
            Genre genre = inputView.readGenre();
            int runningTime = inputView.readRunningTime();
            AgeRating ageRating = inputView.readAgeRating();
            LocalDate releaseDate = inputView.readReleaseDate();

            Movie movie = movieService.registerMovie(title, genre, runningTime, ageRating, releaseDate);
            System.out.println(movie.getId() + "번 영화 '" + movie.getTitle() + "'이(가) 등록되었습니다.");
        } catch (IllegalArgumentException e) {
            // 입력 변환 실패, Movie 검증 실패
            System.out.println(e.getMessage());
        }
    }

    // 영화 수정 (관리자)
    public void updateMovie() {
        // 번호를 고르기 쉽게 목록을 먼저 보여줌
        showMovies();

        try {
            Long id = inputView.readMovieId();
            // 없는 영화면 여기서 바로 예외 (항목을 다 입력한 뒤에 실패하지 않도록 먼저 확인)
            Movie current = movieService.findMovie(id);

            // Enter만 누른 항목은 null -> 서비스에서 기존 값 유지
            String title = inputView.readTitleForUpdate(current.getTitle());
            Genre genre = inputView.readGenreForUpdate(current.getGenre());
            Integer runningTime = inputView.readRunningTimeForUpdate(current.getRunningTime());
            AgeRating ageRating = inputView.readAgeRatingForUpdate(current.getAgeRating());
            LocalDate releaseDate = inputView.readReleaseDateForUpdate(current.getReleaseDate());

            Movie updated = movieService.updateMovie(id, title, genre, runningTime, ageRating, releaseDate);
            System.out.println(updated.getId() + "번 영화 '" + updated.getTitle() + "'이(가) 수정되었습니다.");
        } catch (IllegalArgumentException e) {
            // 숫자가 아닌 입력, 없는 영화, 입력 변환 실패, Movie 검증 실패
            System.out.println(e.getMessage());
        }
    }

    // 영화 삭제 (관리자)
    public void deleteMovie() {

        showMovies();

        try {
            Long id = inputView.readMovieId();
            movieService.deleteMovie(id);
            System.out.println(id + "번 영화가 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            // 숫자가 아닌 입력, 없는 영화
            System.out.println(e.getMessage());
        } catch (IllegalStateException e) {
            // 스케줄이 있는 영화
            System.out.println(e.getMessage());
        }
    }
}
