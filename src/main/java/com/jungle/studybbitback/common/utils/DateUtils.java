package com.jungle.studybbitback.common.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class DateUtils {

    // 한국어 요일 문자열과 DayOfWeek 매핑
    private static final Map<String, DayOfWeek> KOREAN_DAY_MAPPING = Map.of(
            "월", DayOfWeek.MONDAY,
            "화", DayOfWeek.TUESDAY,
            "수", DayOfWeek.WEDNESDAY,
            "목", DayOfWeek.THURSDAY,
            "금", DayOfWeek.FRIDAY,
            "토", DayOfWeek.SATURDAY,
            "일", DayOfWeek.SUNDAY
    );

    // 요일 한국어 표기 단일단위
    public static String getDayInKorean(DayOfWeek day) {
        return day.getDisplayName(TextStyle.FULL, Locale.KOREAN);
    }

    /**
     * 한국어 요일 문자열을 DayOfWeek 리스트로 변환.
     * 예: "월,수" -> [MONDAY, WEDNESDAY]
     */
    public static List<DayOfWeek> parseDaysOfWeek(String daysOfWeek) {
        if (daysOfWeek == null || daysOfWeek.isBlank()) {
            throw new IllegalArgumentException("daysOfWeek 값이 비어있습니다.");
        }

        return Arrays.stream(daysOfWeek.split(","))
                .map(day -> {
                    DayOfWeek mappedDay = KOREAN_DAY_MAPPING.get(day.trim());
                    if (mappedDay == null) {
                        throw new IllegalArgumentException("유효하지 않은 요일 값입니다: " + day);
                    }
                    return mappedDay;
                })
                .collect(Collectors.toList());
    }

    /**
     * 날짜(LocalDate)와 시간(LocalTime)를 병합하여 LocalDateTime 생성.
     *
     * @param date LocalDate - 기준 날짜
     * @param time LocalTime - 기준 시간
     * @return LocalDateTime - 병합된 날짜와 시간
     */
    public static LocalDateTime mergeDateAndTime(LocalDate date, LocalTime time) {
        if (date == null || time == null) {
            throw new IllegalArgumentException("날짜와 시간을 모두 입력해 주세요.");
        }
        return date.atTime(time);
    }

    /**
     * 시작 시간과 종료 시간이 동일한 날짜에 있는지 확인.
     *
     * @param startDateTime 시작 시간
     * @param endDateTime   종료 시간
     */
    public static void validateSameDate(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (!startDateTime.toLocalDate().equals(endDateTime.toLocalDate())) {
            throw new IllegalArgumentException("시작 시간과 종료 시간은 동일한 날짜에 있어야 합니다.");
        }
    }

    /**
     * 지정된 날짜로부터 다음 특정 요일로 이동.
     *
     * @param startDate 시작 날짜
     * @param targetDay 목표 요일
     * @return 다음 해당 요일 날짜
     */
    public static LocalDate getNextWeekday(LocalDate startDate, DayOfWeek targetDay) {
        int daysUntilNext = (targetDay.getValue() - startDate.getDayOfWeek().getValue() + 7) % 7;
        return startDate.plusDays(daysUntilNext);
    }
}
