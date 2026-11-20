package com.team02.cinema.movie.controller;

import com.team02.cinema.movie.domain.schedule.Schedule;
import com.team02.cinema.movie.service.MovieService;
import com.team02.cinema.movie.service.ScheduleService;
import com.team02.cinema.movie.view.MovieOutputView;
import com.team02.cinema.movie.view.ScheduleInputView;
import com.team02.cinema.movie.view.ScheduleOutputView;
import com.team02.cinema.theater.domain.Screen;
import com.team02.cinema.theater.service.TheaterService;

import java.time.LocalDateTime;
import java.util.List;

// 스케줄 메뉴 기능을 담당 (입력 -> 서비스 호출 -> 결과 출력)
// 예외는 여기서 잡아 메시지만 출력하고, 프로그램은 계속 실행되도록 함
public class ScheduleController {

    private final ScheduleService scheduleService;

    private final MovieService movieService;

    private final TheaterService theaterService;
    private final ScheduleInputView inputView;
    private final ScheduleOutputView outputView;
    private final MovieOutputView movieOutputView;

    public ScheduleController(ScheduleService scheduleService, MovieService movieService,
                              TheaterService theaterService, ScheduleInputView inputView,
                              ScheduleOutputView outputView, MovieOutputView movieOutputView) {
        this.scheduleService = scheduleService;
        this.movieService = movieService;
        this.theaterService = theaterService;
        this.inputView = inputView;
        this.outputView = outputView;
        this.movieOutputView = movieOutputView;
    }

    // 전체 스케줄 목록
    public void showSchedules() {
        List<Schedule> schedules = scheduleService.findAllSchedules();
        outputView.printScheduleList(schedules);
    }

    // 스케줄 등록 (관리자)
    public void registerSchedule() {
        // 영화 번호를 고르기 쉽게 영화 목록을 먼저 보여줌
        movieOutputView.printMovieList(movieService.findAllMovies());

        try {
            Long movieId = inputView.readMovieId();
            int screenNum = inputView.readScreenNum();
            // 없는 상영관이면 여기서 예외
            Screen screen = theaterService.getScreen(screenNum);
            LocalDateTime startTime = inputView.readStartTime();
            int basePrice = inputView.readBasePrice();

            Schedule schedule = scheduleService.registerSchedule(movieId, screen, startTime, basePrice);
            System.out.println(schedule.getId() + "번 스케줄이 등록되었습니다. ("
                    + schedule.getMovie().getTitle() + ", "
                    + schedule.getStartTime().toLocalDate() + " "
                    + schedule.getStartTime().toLocalTime() + ")");
        } catch (IllegalArgumentException e) {
            // 숫자가 아닌 입력, 없는 영화, 없는 상영관, 날짜 형식 오류, Schedule 검증 실패
            System.out.println(e.getMessage());
        } catch (IllegalStateException e) {
            // 기존 스케줄과 상영 시간이 겹침
            System.out.println(e.getMessage());
        }
    }

    // 스케줄 삭제 (관리자)
    public void deleteSchedule() {
        // 번호를 고르기 쉽게 목록을 먼저 보여줌
        showSchedules();

        try {
            Long id = inputView.readScheduleId();
            scheduleService.deleteSchedule(id);
            System.out.println(id + "번 스케줄이 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            // 숫자가 아닌 입력, 없는 스케줄
            System.out.println(e.getMessage());
        } catch (IllegalStateException e) {
            // 예매가 있는 스케줄
            System.out.println(e.getMessage());
        }
    }
}
