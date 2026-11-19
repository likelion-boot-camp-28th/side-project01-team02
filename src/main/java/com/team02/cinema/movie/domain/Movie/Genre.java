package com.team02.cinema.movie.domain.Movie;

public enum Genre {
    DRAMA("드라마"),
    ACTION("액션"),
    CRIME("범죄"),
    THRILLER("스릴러"),
    HORROR("공포"),
    COMEDY("코미디"),
    FANTASY("판타지"),
    SF("SF"),
    ANIMATION("애니메이션"),
    ROMANCE("로맨스");

    private final String genre;

    Genre(String genreName) {
        this.genre = genreName;
    }

    public String getGenre() {
        return genre;
    }
}
