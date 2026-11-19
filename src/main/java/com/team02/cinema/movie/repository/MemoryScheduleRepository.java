package com.team02.cinema.movie.repository;

import com.team02.cinema.movie.domain.schedule.Schedule;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class MemoryScheduleRepository implements ScheduleRepository {

    private final Map<Long, Schedule> schedules = new LinkedHashMap<>();
    private long lastId = 0;

    @Override
    public Schedule save(Schedule schedule) {
        Schedule saved = schedule;
        if (schedule.getId() == null) {
            // 신규 등록: movie와 동일
            saved = new Schedule(lastId + 1, schedule.getMovie(), schedule.getScreen(),
                    schedule.getStartTime(), schedule.getBasePrice());
        }
        // movie와 동일
        lastId = Math.max(lastId, saved.getId());
        schedules.put(saved.getId(), saved);
        return saved;
    }

    @Override
    public List<Schedule> findAll() {
        return List.copyOf(schedules.values());
    }

    @Override
    public Optional<Schedule> findById(Long id) {
        return Optional.ofNullable(schedules.get(id));
    }

    @Override
    public boolean deleteById(Long id) {
        return schedules.remove(id) != null;
    }

    @Override
    public List<Schedule> findByMovieId(Long movieId) {
        return schedules.values().stream()
                .filter(schedule -> isSameMovie(schedule, movieId))
                .toList();
    }

    @Override
    public boolean existsByMovieId(Long movieId) {
        // 하나라도 찾으면 거기서 멈춘다
        return schedules.values().stream()
                .anyMatch(schedule -> isSameMovie(schedule, movieId));
    }

    // 영화는 수정 시 새 객체로 바뀌므로 객체가 아닌 id로 비교
    // Long은 객체라 == 대신 equals로 값을 비교한다
    private boolean isSameMovie(Schedule schedule, Long movieId) {
        return Objects.equals(schedule.getMovie().getId(), movieId);
    }
}
