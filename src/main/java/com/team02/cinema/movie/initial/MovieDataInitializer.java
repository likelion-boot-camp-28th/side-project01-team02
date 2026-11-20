package com.team02.cinema.movie.initial;

import com.team02.cinema.movie.domain.Movie.Movie;
import com.team02.cinema.movie.service.MovieService;
import com.team02.cinema.movie.service.ScheduleService;
import com.team02.cinema.theater.service.TheaterService;

import java.nio.file.Path;
import java.util.List;

// 프로그램 시작 시 CSV 초기 데이터(영화 -> 스케줄)를 등록
// 메뉴(Controller)가 쓰는 것과 같은 서비스 객체를 넘겨받아야 데이터가 공유됨
public class MovieDataInitializer {

    private static final Path MOVIE_FILE = Path.of("data/movies.csv");
    private static final Path SCHEDULE_FILE = Path.of("data/schedules.csv");

    private final MovieService movieService;
    private final ScheduleService scheduleService;
    // 스케줄 CSV의 상영관 번호로 상영관을 찾기 위해 사용 (상영관이 먼저 등록되어 있어야 함)
    private final TheaterService theaterService;

    public MovieDataInitializer(MovieService movieService, ScheduleService scheduleService,
                                TheaterService theaterService) {
        this.movieService = movieService;
        this.scheduleService = scheduleService;
        this.theaterService = theaterService;
    }


    public void init() {
        // 영화 먼저 등록 (스케줄이 영화를 제목으로 찾기 때문에 순서 지켜야 함)
        MovieCsvLoader movieLoader = new MovieCsvLoader(movieService);
        List<Movie> movies = movieLoader.load(MOVIE_FILE);

        // 등록된 영화 목록을 넘겨서 스케줄 등록
        ScheduleCsvLoader scheduleLoader = new ScheduleCsvLoader(scheduleService, theaterService);
        scheduleLoader.load(SCHEDULE_FILE, movies);
    }
}
