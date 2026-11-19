package com.team02.cinema.theater.repository;

import com.team02.cinema.theater.domain.Screen;

import java.util.Map;
import java.util.Optional;

public interface ScreenRepository {
    void save(int num, Screen screen);

    Map<Integer, Screen> findAll();

    Optional<Screen> findByNum(int num);

    boolean deleteByNum(int num);
}
