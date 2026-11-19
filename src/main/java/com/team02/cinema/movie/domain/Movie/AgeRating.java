package com.team02.cinema.movie.domain.Movie;

public enum AgeRating {
    ALL("전체 관람가", 0),
    AGE_12("12세 이상 관람가", 12),
    AGE_15("15세 이상 관람가", 15),
    AGE_19("청소년 관람불가", 19);

    private final String rating;
    private final int minAge;

    AgeRating(String rating, int minAge) {
        this.rating = rating;
        this.minAge = minAge;
    }

    public boolean isAllowed(int age) {
        return age >= minAge;
    }

    public String getRating() {
        return rating;
    }

    public int getMinAge() {
        return minAge;
    }
}
