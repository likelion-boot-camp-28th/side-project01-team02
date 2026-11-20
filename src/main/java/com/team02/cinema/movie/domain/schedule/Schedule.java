package com.team02.cinema.movie.domain.schedule;

import com.team02.cinema.movie.domain.Movie.Movie;
import com.team02.cinema.theater.domain.Screen;

import java.time.LocalDateTime;

public class Schedule {

    private static final int CLEANING_MINUTES = 10;

    private final Long id;
    private final Movie movie;
    private final Screen screen;
    private final LocalDateTime startTime;
    private final int basePrice;

    public Schedule(Long id, Movie movie, Screen screen, LocalDateTime startTime, int basePrice) {
        validate(movie, screen, startTime, basePrice);
        this.id = id;
        this.movie = movie;
        this.screen = screen;
        this.startTime = startTime;
        this.basePrice = basePrice;
    }

    private void validate(Movie movie, Screen screen, LocalDateTime startTime, int basePrice) {
        if (movie == null) {
            throw new IllegalArgumentException("영화는 필수입니다.");
        }
        if (screen == null) {
            throw new IllegalArgumentException("상영관은 필수입니다.");
        }
        if (startTime == null) {
            throw new IllegalArgumentException("시작 시각은 필수입니다.");
        }
        if (basePrice <= 0) {
            throw new IllegalArgumentException("기본 가격은 0원보다 커야 합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Movie getMovie() {
        return movie;
    }

    public Screen getScreen() {
        return screen;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public int getBasePrice() {
        return basePrice;
    }

    // 영화가 끝나는 시각 (시작 + 상영시간) - 사용자에게 보여주는 종료 시각
    public LocalDateTime getMovieEndTime() {
        return startTime.plusMinutes(movie.getRunningTime());
    }

    // 이 회차가 상영관을 비워 주는 시각 (시작 + 상영시간 + 청소 시간) - 스케줄 겹침 검사용
    public LocalDateTime getEndTime() {
        return getMovieEndTime().plusMinutes(CLEANING_MINUTES);
    }

    // 두 회차의 시작, 종료 구간이 겹치는지 판단 - 1의 종료시각과 2의 시작 시작이 같다면 겹치지 않는 것으로 보겠습니다.
    public boolean overlaps(Schedule other) {
        return startTime.isBefore(other.getEndTime())
                && other.startTime.isBefore(getEndTime());
    }
}
