package com.team02.cinema.movie.service;

import com.team02.cinema.movie.domain.Movie.Movie;
import com.team02.cinema.movie.domain.schedule.Schedule;
import com.team02.cinema.movie.repository.MovieRepository;
import com.team02.cinema.movie.repository.ScheduleRepository;
import com.team02.cinema.theater.domain.Screen;

import java.time.LocalDateTime;
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
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 영화입니다. id=" + movieId));

        // overlaps()에 넘기기 위한 객체
        Schedule newSchedule = new Schedule(null, movie, screen, startTime, basePrice);

        // 같은 상영관의 스케줄끼리만 시간이 겹치는지 비교한다 (확장 가능성)
        scheduleRepository.findAll().stream()
                .filter(existing -> existing.getScreen().getNum() == screen.getNum())
                .filter(existing -> existing.overlaps(newSchedule))
                .findFirst()
                .ifPresent(existing -> {
                    throw new IllegalStateException(
                            "상영 시간이 기존 스케줄과 겹칩니다. 겹치는 스케줄 id=" + existing.getId()
                                    + " (" + existing.getStartTime() + " ~ " + existing.getEndTime() + ")");
                });

        return scheduleRepository.save(newSchedule);
    }

    // 스케줄 삭제
    public void deleteSchedule(Long id) {
        if (scheduleRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 스케줄입니다. id=" + id);
        }
        if (reservationChecker.hasReservation(id)) {
            throw new IllegalStateException("예매가 있는 스케줄은 삭제할 수 없습니다. id=" + id);
        }
        scheduleRepository.deleteById(id);
    }

    // 전체 스케줄 목록
    public List<Schedule> findAllSchedules() {
        return scheduleRepository.findAll();
    }

    // 영화 상세 조회
    public List<Schedule> findSchedulesByMovie(Long movieId) {
        return scheduleRepository.findByMovieId(movieId);
    }
}
