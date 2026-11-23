package com.team02.cinema.movie.service;

import com.team02.cinema.movie.domain.Movie.Movie;
import com.team02.cinema.movie.domain.schedule.Schedule;
import com.team02.cinema.movie.repository.MovieRepository;
import com.team02.cinema.movie.repository.ScheduleRepository;
import com.team02.cinema.theater.domain.Screen;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    // 스케줄 등록 시 영화 id로 영화를 찾기 위해 사용
    private final MovieRepository movieRepository;
    // 스케줄 삭제 시 예매 존재 여부 확인용 (예매 쪽 구현을 바깥에서 넘겨받는다)
    private final ReservationChecker reservationChecker;

    public ScheduleService(ScheduleRepository scheduleRepository, MovieRepository movieRepository,
                           ReservationChecker reservationChecker) {
        this.scheduleRepository = scheduleRepository;
        this.movieRepository = movieRepository;
        this.reservationChecker = reservationChecker;
    }

    // 스케줄 등록: 상영관(Screen)은 호출하는 쪽에서 TheaterService로 찾아서 넘겨주도록 함
    public Schedule registerSchedule(Long movieId, Screen screen, LocalDateTime startTime, int basePrice) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 영화입니다."));

        // overlaps()에 넘기기 위한 객체
        Schedule newSchedule = new Schedule(null, movie, screen, startTime, basePrice);

        // 같은 상영관의 스케줄끼리만 시간이 겹치는지 비교한다 (확장 가능성)
        scheduleRepository.findAll().stream()
                .filter(existing -> existing.getScreen().getNum() == screen.getNum())
                .filter(existing -> existing.overlaps(newSchedule))
                .findFirst()
                .ifPresent(existing -> {
                    // 날짜와 시각을 나눠서 붙임 (LocalDateTime을 그대로 붙이면 사이에 'T'가 출력됨)
                    // 목록과 같은 영화 종료 시각을 보여주고, 실제로 막히는 이유인 청소 종료 시각을 함께 알려줌
                    // 예: 상영 시간이 기존 스케줄과 겹칩니다. (6번 범죄도시, 2026-11-21 13:00 ~ 15:01, 청소 시간 포함 15:11까지 사용)
                    throw new IllegalStateException(
                            "상영 시간이 기존 스케줄과 겹칩니다. ("
                                    + existing.getId() + "번 " + existing.getMovie().getTitle() + ", "
                                    + existing.getStartTime().toLocalDate() + " "
                                    + existing.getStartTime().toLocalTime() + " ~ "
                                    + existing.getMovieEndTime().toLocalTime() + ", 청소 시간 포함 "
                                    + existing.getEndTime().toLocalTime() + "까지 사용)");
                });

        return scheduleRepository.save(newSchedule);
    }

    // 스케줄 삭제
    public void deleteSchedule(Long id) {
        if (scheduleRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 스케줄입니다.");
        }
        if (reservationChecker.hasReservation(id)) {
            throw new IllegalStateException("예매가 있는 스케줄은 삭제할 수 없습니다.");
        }
        scheduleRepository.deleteById(id);
    }

    // 스케줄 단건 조회: 예매하기에서 상영 번호로 스케줄을 찾을 때 사용
    public Schedule findSchedule(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스케줄입니다."));
    }

    // 고른 영화의 스케줄인지 확인하면서 조회: 예매하기에서 다른 영화의 상영 번호를 넣는 것을 막기 위해 사용
    public Schedule findScheduleOfMovie(Long movieId, Long scheduleId) {
        Schedule schedule = findSchedule(scheduleId);

        // Long은 객체라서 == 대신 equals로 값을 비교한다
        if (!schedule.getMovie().getId().equals(movieId)) {
            throw new IllegalArgumentException("선택한 영화의 상영 스케줄이 아닙니다.");
        }
        return schedule;
    }

    // 전체 스케줄 목록 (관리자용): 지난 상영도 확인·삭제할 수 있도록 모두 보여주고 시간순 정렬만 한다
    public List<Schedule> findAllSchedules() {
        return sortByStartTime(scheduleRepository.findAll());
    }

    // 영화 상세 조회 (사용자용): 아직 시작하지 않은 상영만 시간순으로 보여준다
    public List<Schedule> findUpcomingSchedulesByMovie(Long movieId) {
        LocalDateTime now = LocalDateTime.now();

        List<Schedule> upcoming = new ArrayList<>();
        for (Schedule schedule : scheduleRepository.findByMovieId(movieId)) {
            // 시작 시각이 지금보다 뒤인 상영만 담는다 (이미 시작한 상영은 예매할 수 없으므로 제외)
            if (schedule.getStartTime().isAfter(now)) {
                upcoming.add(schedule);
            }
        }
        return sortByStartTime(upcoming);
    }

    // 시작 시각이 빠른 순으로 정렬, 시작 시각이 같으면 상영관 번호가 작은 순
    private List<Schedule> sortByStartTime(List<Schedule> schedules) {
        // Repository가 준 목록은 수정할 수 없을 수 있어서 새 리스트에 복사한 뒤 정렬
        List<Schedule> sorted = new ArrayList<>(schedules);
        sorted.sort(Comparator.comparing(Schedule::getStartTime)
                .thenComparingInt(schedule -> schedule.getScreen().getNum()));
        return sorted;
    }
}
