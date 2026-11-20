package com.team02.cinema.movie.view;

import com.team02.cinema.movie.domain.schedule.Schedule;

import java.util.List;

public class ScheduleOutputView {

    // 전체 스케줄 목록 (관리자 메뉴에서 사용)
    public void printScheduleList(List<Schedule> schedules) {
        if (schedules.isEmpty()) {
            System.out.println("등록된 스케줄이 없습니다.");
            return;
        }

        System.out.println("===== 스케줄 목록 =====");
        for (Schedule schedule : schedules) {
            // 예: 3. 파묘 | 1관 | 2026-11-21 16:00 ~ 18:24 | 14000원
            System.out.println(makeScheduleText(schedule));
        }
    }

    // 영화 상세의 스케줄 목록 제목
    public void printDetailScheduleHeader() {
        System.out.println("===== 상영 스케줄 =====");
    }

    // 영화 상세에 보여줄 스케줄 한 줄 (잔여 좌석 수 포함)
    public void printScheduleWithSeats(Schedule schedule, int availableSeats) {
        // 상영관 전체 좌석 수 = 행 수 x 열 수
        int totalSeats = schedule.getScreen().getRow() * schedule.getScreen().getCol();

        // 예: 3. 파묘 | 1관 | 2026-11-21 16:00 ~ 18:24 | 14000원 | 잔여 23/25석
        System.out.println(makeScheduleText(schedule)
                + " | 잔여 " + availableSeats + "/" + totalSeats + "석");
    }

    // 두 출력에서 같이 쓰는 스케줄 기본 정보 문장
    private String makeScheduleText(Schedule schedule) {
        return schedule.getId() + ". "
                + schedule.getMovie().getTitle() + " | "
                + schedule.getScreen().getNum() + "관 | "
                + schedule.getStartTime().toLocalDate() + " "
                + schedule.getStartTime().toLocalTime() + " ~ "
                + schedule.getEndTime().toLocalTime() + " | "
                + schedule.getBasePrice() + "원";
    }
}
