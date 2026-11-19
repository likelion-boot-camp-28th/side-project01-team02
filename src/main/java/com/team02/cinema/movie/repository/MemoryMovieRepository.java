package com.team02.cinema.movie.repository;

import com.team02.cinema.movie.domain.Movie.Movie;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MemoryMovieRepository implements MovieRepository {


    private final Map<Long, Movie> movies = new LinkedHashMap<>();
    private long lastId = 0;

    @Override
    public Movie save(Movie movie) {
        Movie saved = movie;
        if (movie.getId() == null) {
            // 신규 등록: Movie의 id가 final이라 새 id를 붙인 객체를 새로 만든다
            saved = new Movie(lastId + 1, movie.getTitle(), movie.getGenre(),
                    movie.getRunningTime(), movie.getAgeRating(), movie.getReleaseDate());
        }
        // CSV처럼 id가 정해진 채로 들어와도 다음 번호가 겹치지 않게 따라 올린다
        lastId = Math.max(lastId, saved.getId());
        movies.put(saved.getId(), saved);
        return saved;
    }

    @Override
    public List<Movie> findAll() {
        // 원본 Map과 연결되지 않은, 수정할 수 없는 복사본을 돌려준다
        return List.copyOf(movies.values());
    }

    @Override
    public Optional<Movie> findById(Long id) {
        return Optional.ofNullable(movies.get(id));
    }

    @Override
    public boolean deleteById(Long id) {
        return movies.remove(id) != null;
    }
}
