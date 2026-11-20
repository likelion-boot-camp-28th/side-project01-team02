package com.team02.cinema.movie.view;

import com.team02.cinema.movie.domain.schedule.Schedule;

import java.util.List;

public class ScheduleOutputView {

    public void printScheduleList(List<Schedule> schedules) {
        if (schedules.isEmpty()) {
            System.out.println("등록된 스케줄이 없습니다.");
            return;
        }

        System.out.println("===== 스케줄 목록 =====");
        for (Schedule schedule : schedules) {
            // 예: 3. 파묘 | 1관 | 2026-11-21 16:00 ~ 18:24 | 14000원
            System.out.println(schedule.getId() + ". "
                    + schedule.getMovie().getTitle() + " | "
                    + schedule.getScreen().getNum() + "관 | "
                    + schedule.getStartTime().toLocalDate() + " "
                    + schedule.getStartTime().toLocalTime() + " ~ "
                    + schedule.getEndTime().toLocalTime() + " | "
                    + schedule.getBasePrice() + "원");
        }
    }
}
