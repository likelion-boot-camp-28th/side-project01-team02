package com.team02.cinema.theater.service;

import com.team02.cinema.theater.domain.Screen;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TheaterService {
    private final Map<Integer, Screen> screens = new TreeMap<>();

    public void addScreen(int num, int row, int col){
        screens.put(num, new Screen(num, row, col));
    }

    public Collection<Screen> getScreens() {
        return List.copyOf(screens.values());
    }

    public Screen getScreen(int num){
        Screen screen = screens.get(num);
        if (screen == null) {
            throw new IllegalArgumentException("존재하지 않는 상영관입니다.");
        }
        return screen;
    }
}
