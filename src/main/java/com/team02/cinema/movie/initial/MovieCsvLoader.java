package com.team02.cinema.movie.initial;

import com.team02.cinema.movie.domain.Movie.AgeRating;
import com.team02.cinema.movie.domain.Movie.Genre;
import com.team02.cinema.movie.domain.Movie.Movie;
import com.team02.cinema.movie.service.MovieService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class MovieCsvLoader {

    private static final int COLUMN_COUNT = 6; // id,title,genre,runningTime,ageRating,releaseDate

    private final MovieService movieService;

    public MovieCsvLoader(MovieService movieService) {
        this.movieService = movieService;
    }

    // CSV를 읽어 영화를 등록하고, 등록에 성공한 영화 목록을 반환한다
    public List<Movie> load(Path path) {
        List<String> lines;
        try {
            lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            // 파일을 못 읽으면 안내만 하고 빈 목록을 줌
            System.out.println("[초기 데이터] 영화 파일을 읽을 수 없습니다: " + path);
            return new ArrayList<>();
        }

        List<Movie> registered = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) { // 0번째 줄은 헤더라 건너뜀
            String line = lines.get(i);
            int lineNumber = i + 1; // 사람이 보는 파일 줄 번호는 1부터 시작

            // 빈 줄은 건너뛴다
            if (line.isBlank()) {
                continue;
            }

            try {
                Movie movie = parseAndRegister(line);
                registered.add(movie);
            } catch (DateTimeParseException e) {
                // 날짜 형식 오류는 IllegalArgumentException이 아니라서 따로 잡도록 함
                System.out.println("[초기 데이터] " + lineNumber + "번째 줄: 날짜 형식이 잘못되었습니다.");
            } catch (IllegalArgumentException e) {
                // 숫자 변환 실패, enum 이름 오류, Movie 검증 실패
                System.out.println("[초기 데이터] " + lineNumber + "번째 줄: " + e.getMessage());
            }
        }

        System.out.println("[초기 데이터] 영화 " + registered.size() + "편 등록 완료");
        return registered;
    }

    private Movie parseAndRegister(String line) {
        // 쉼표로 나누고 열 개수 확인
        String[] values = line.split(",");
        if (values.length != COLUMN_COUNT) {
            throw new IllegalArgumentException("열 개수가 " + COLUMN_COUNT + "개가 아닙니다.");
        }

        // 각 값의 앞뒤 공백 제거 (id는 제외)
        String title = values[1].trim();
        String genreText = values[2].trim();
        String runningTimeText = values[3].trim();
        String ageRatingText = values[4].trim();
        String releaseDateText = values[5].trim();

        // 문자열을 알맞은 타입으로 변환
        Genre genre = Genre.valueOf(genreText);
        int runningTime = Integer.parseInt(runningTimeText);
        AgeRating ageRating = AgeRating.valueOf(ageRatingText);
        LocalDate releaseDate = LocalDate.parse(releaseDateText);

        // 서비스를 통해 등록
        return movieService.registerMovie(title, genre, runningTime, ageRating, releaseDate);
    }
}
