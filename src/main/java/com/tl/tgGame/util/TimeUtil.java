package com.tl.tgGame.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

public class TimeUtil {
    /**
     * 获取今天零点时间
     *
     * @return 时间
     */
    public static LocalDateTime getTodayBegin() {
        return LocalDate.now().atStartOfDay();
    }

    public static LocalDateTime getWeekBegin() {
        LocalDateTime now = LocalDate.now().atStartOfDay();
        return now.minusDays(now.getDayOfWeek().getValue() - 1);
    }

    public static LocalDateTime getMonthBegin() {
        return LocalDate.now().with(ChronoField.DAY_OF_MONTH, 1).atStartOfDay();
    }
}
