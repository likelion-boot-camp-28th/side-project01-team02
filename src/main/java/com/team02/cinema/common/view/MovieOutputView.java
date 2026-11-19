package com.team02.cinema.common.view;

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
            System.out.printf("%d. %s | %s | %d분 | %s%n",
                    movie.getId(),
                    movie.getTitle(),
                    movie.getGenre().getGenre(),
                    movie.getRunningTime(),
                    movie.getAgeRating().getRating());
        }
    }
}
