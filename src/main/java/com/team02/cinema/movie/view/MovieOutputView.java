package com.team02.cinema.movie.view;

import com.team02.cinema.movie.domain.Movie.Movie;

import java.util.List;

public class MovieOutputView {

    public void printMovieList(List<Movie> movies) {
        if (movies.isEmpty()) {
            System.out.println("등록된 영화가 없습니다.");
            return;
        }

        System.out.println("===== 영화 목록 =====");
        for (Movie movie : movies) {
            System.out.println(movie.getId() + ". "
                    + movie.getTitle() + " | "
                    + movie.getGenre().getGenre() + " | "
                    + movie.getRunningTime() + "분 | "
                    + movie.getAgeRating().getRating());
        }
    }

    // 영화 한 편의 상세 정보
    public void printMovieDetail(Movie movie) {
        System.out.println("===== 영화 상세 =====");
        System.out.println("제목 : " + movie.getTitle());
        System.out.println("장르 : " + movie.getGenre().getGenre());
        System.out.println("상영시간 : " + movie.getRunningTime() + "분");
        System.out.println("관람등급 : " + movie.getAgeRating().getRating());
        System.out.println("개봉일 : " + movie.getReleaseDate());
    }
}
