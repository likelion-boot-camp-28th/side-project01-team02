package com.team02.cinema.movie.domain.Movie;

import java.time.LocalDate;

public class Movie {

    private final Long id;
    private String title;
    private Genre genre;
    private int runningTime;
    private AgeRating ageRating;
    private LocalDate releaseDate;

    public Movie(Long id, String title, Genre genre, int runningTime, AgeRating ageRating, LocalDate releaseDate) {
        validate(title, genre, runningTime, ageRating, releaseDate);
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.runningTime = runningTime;
        this.ageRating = ageRating;
        this.releaseDate = releaseDate;
    }

    private void validate(String title, Genre genre, int runningTime, AgeRating ageRating, LocalDate releaseDate) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("영화 제목은 비어 있을 수 없습니다.");
        }
        if (genre == null) {
            throw new IllegalArgumentException("장르는 필수입니다.");
        }
        if (runningTime <= 0) {
            throw new IllegalArgumentException("상영시간은 1분 이상이어야 합니다.");
        }
        if (ageRating == null) {
            throw new IllegalArgumentException("관람등급은 필수입니다.");
        }
        if (releaseDate == null) {
            throw new IllegalArgumentException("개봉일은 필수입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Genre getGenre() {
        return genre;
    }

    public int getRunningTime() {
        return runningTime;
    }

    public AgeRating getAgeRating() {
        return ageRating;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }
}
