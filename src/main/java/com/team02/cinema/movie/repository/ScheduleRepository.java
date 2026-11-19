package com.team02.cinema.movie.repository;

import com.team02.cinema.movie.domain.schedule.Schedule;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository {

    // 같은 id가 있으면 덮어쓴다
    Schedule save(Schedule schedule);

    // 전체 스케줄 목록
    List<Schedule> findAll();

    // id로 스케줄 한 건 찾기
    Optional<Schedule> findById(Long id);

    // id로 삭제 (실제로 지웠으면 true, 없던 id면 false)
    boolean deleteById(Long id);

    // 특정 영화의 스케줄 목록 (영화 상세 조회 기능)
    List<Schedule> findByMovieId(Long movieId);

    // 특정 영화를 쓰는 스케줄이 하나라도 있는지 (삭제 방지)
    boolean existsByMovieId(Long movieId);
}
