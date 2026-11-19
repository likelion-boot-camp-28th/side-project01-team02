package com.team02.cinema.movie.repository;

import com.team02.cinema.movie.domain.Movie.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieRepository {

    // 같은 id가 있으면 덮어쓴다
    Movie save(Movie movie);

    // 전체 영화 목록
    List<Movie> findAll();

    // id로 영화 한 편 찾기
    Optional<Movie> findById(Long id);

    // id로 삭제 (실제로 지웠으면 true, 없던 id면 false)
    boolean deleteById(Long id);
}
