package com.team02.cinema.movie.initial;

import com.team02.cinema.movie.domain.Movie.Movie;
import com.team02.cinema.movie.domain.schedule.Schedule;
import com.team02.cinema.movie.service.ScheduleService;
import com.team02.cinema.theater.domain.Screen;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ScheduleCsvLoader {

    private static final int COLUMN_COUNT = 4; // dayOffset,startTime,movieTitle,basePrice

    private final ScheduleService scheduleService;

    private final Screen screen;

    public ScheduleCsvLoader(ScheduleService scheduleService, Screen screen) {
        this.scheduleService = scheduleService;
        this.screen = screen;
    }

    // CSV를 읽어 스케줄을 등록하고, 등록에 성공한 스케줄 목록을 반환
    // movies: MovieCsvLoader가 돌려준 영화 목록 (제목으로 영화를 찾을 때 사용)
    public List<Schedule> load(Path path, List<Movie> movies) {
        List<String> lines;
        try {
            lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            // 파일을 못 읽으면 안내만 하고 빈 목록을 줌
            System.out.println("[초기 데이터] 스케줄 파일을 읽을 수 없습니다: " + path);
            return new ArrayList<>();
        }

        List<Schedule> registered = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) { // 0번째 줄은 헤더라 건너뜀
            String line = lines.get(i);
            int lineNumber = i + 1; // 사람이 보는 파일 줄 번호는 1부터 시작

            // 빈 줄은 건너뜀
            if (line.isBlank()) {
                continue;
            }

            try {
                Schedule schedule = parseAndRegister(line, movies);
                registered.add(schedule);
            } catch (DateTimeParseException e) {
                // 시각 형식 오류는 IllegalArgumentException이 아니라서 따로 잡도록 함
                System.out.println("[초기 데이터] " + lineNumber + "번째 줄: 시각 형식이 잘못되었습니다.");
            } catch (IllegalArgumentException e) {
                // 숫자 변환 실패, 없는 영화 제목, Schedule 검증 실패
                System.out.println("[초기 데이터] " + lineNumber + "번째 줄: " + e.getMessage());
            } catch (IllegalStateException e) {
                // 기존 스케줄과 상영 시간이 겹침
                System.out.println("[초기 데이터] " + lineNumber + "번째 줄: " + e.getMessage());
            }
        }

        System.out.println("[초기 데이터] 스케줄 " + registered.size() + "개 등록 완료");
        return registered;
    }

    private Schedule parseAndRegister(String line, List<Movie> movies) {
        // 쉼표로 나누고 열 개수 확인
        String[] values = line.split(",");
        if (values.length != COLUMN_COUNT) {
            throw new IllegalArgumentException("열 개수가 " + COLUMN_COUNT + "개가 아닙니다.");
        }

        // 각 값의 앞뒤 공백 제거
        String dayOffsetText = values[0].trim();
        String startTimeText = values[1].trim();
        String movieTitle = values[2].trim();
        String basePriceText = values[3].trim();

        // 문자열을 알맞은 타입으로 변환
        int dayOffset = Integer.parseInt(dayOffsetText);
        LocalTime time = LocalTime.parse(startTimeText);
        int basePrice = Integer.parseInt(basePriceText);

        // 오늘 날짜 + dayOffset 일 뒤의 날짜에 시각을 붙임
        LocalDate date = LocalDate.now().plusDays(dayOffset);
        LocalDateTime startTime = date.atTime(time);

        // 제목으로 영화 찾기
        Movie movie = findMovieByTitle(movies, movieTitle);

        return register(movie.getId(), startTime, basePrice);
    }

    // 영화 목록에서 제목이 같은 영화를 찾음, 없으면 예외
    private Movie findMovieByTitle(List<Movie> movies, String title) {
        for (Movie movie : movies) {
            if (movie.getTitle().equals(title)) {
                return movie;
            }
        }
        throw new IllegalArgumentException("등록된 영화 중 '" + title + "' 제목이 없습니다.");
    }

    // 스케줄 등록
    private Schedule register(Long movieId, LocalDateTime startTime, int basePrice) {
        return scheduleService.registerSchedule(movieId, screen, startTime, basePrice);
    }
}
