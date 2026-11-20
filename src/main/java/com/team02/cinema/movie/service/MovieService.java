package com.team02.cinema.movie.service;

import com.team02.cinema.movie.domain.Movie.AgeRating;
import com.team02.cinema.movie.domain.Movie.Genre;
import com.team02.cinema.movie.domain.Movie.Movie;
import com.team02.cinema.movie.repository.MovieRepository;
import com.team02.cinema.movie.repository.ScheduleRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class MovieService {

    private final MovieRepository movieRepository;
    // 영화 삭제 시 스케줄 존재 여부 확인용
    private final ScheduleRepository scheduleRepository;

    public MovieService(MovieRepository movieRepository, ScheduleRepository scheduleRepository) {
        this.movieRepository = movieRepository;
        this.scheduleRepository = scheduleRepository;
    }

    // 목록 조회
    public List<Movie> findAllMovies() {
        return movieRepository.findAll();
    }

    // 영화 등록: id는 null로 넘겨 저장소가 새 번호를 붙이게 함
    public Movie registerMovie(String title, Genre genre, int runningTime, AgeRating ageRating, LocalDate releaseDate) {
        Movie movie = new Movie(null, title, genre, runningTime, ageRating, releaseDate);
        return movieRepository.save(movie);
    }

    // 영화 한 편 조회: 없는 id면 예외
    public Movie findMovie(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 영화입니다. id=" + id));
    }

    // 영화 수정: 없는 id면 예외, null로 넘긴 항목은 기존 값을 그대로 유지한다
    public Movie updateMovie(Long id, String title, Genre genre, Integer runningTime, AgeRating ageRating,
                             LocalDate releaseDate) {
        Movie existing = movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 영화입니다. id=" + id));

        Movie updated = new Movie(
                id,
                Objects.requireNonNullElse(title, existing.getTitle()),
                Objects.requireNonNullElse(genre, existing.getGenre()),
                Objects.requireNonNullElse(runningTime, existing.getRunningTime()),
                Objects.requireNonNullElse(ageRating, existing.getAgeRating()),
                Objects.requireNonNullElse(releaseDate, existing.getReleaseDate())
        );
        return movieRepository.save(updated);
    }

    // 영화 삭제: 없는 영화인지 먼저 확인하고, 스케줄이 있는 영화는 삭제할 수 없다
    public void deleteMovie(Long id) {
        if (movieRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 영화입니다. id=" + id);
        }
        if (scheduleRepository.existsByMovieId(id)) {
            throw new IllegalStateException("상영 스케줄이 있는 영화는 삭제할 수 없습니다. id=" + id);
        }
        movieRepository.deleteById(id);
    }
}
