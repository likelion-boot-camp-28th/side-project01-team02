package com.team02.cinema.theater.repository;

import com.team02.cinema.theater.domain.Screen;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class MemoryScreenRepository implements ScreenRepository {

    private final Map<Integer, Screen> screens = new TreeMap<>();

    @Override
    public void save(int num, Screen screen) {
        screens.put(num, screen);
    }

    @Override
    public Map<Integer, Screen> findAll() {
        return Collections.unmodifiableMap(new TreeMap<>(screens));
    }

    @Override
    public Optional<Screen> findByNum(int num) {
        return Optional.ofNullable(screens.get(num));
    }

    @Override
    public boolean deleteByNum(int num) {
        return screens.remove(num) != null;
    }
}
