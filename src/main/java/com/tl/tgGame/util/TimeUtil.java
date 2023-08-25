package com.tl.tgGame.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.TimeZone;

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

    public static LocalDateTime parseLocalDateTime(String dateStr) {
        DateTime parse = DateUtil.parse(dateStr);
        Instant instant = parse.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        System.out.println(localDateTime);
        return localDateTime;
    }


    /**
     * LocalDateTime 转换 美东时间 uct-4
     */
    public static String americaCharge(LocalDateTime time) {
        ZoneId of2 = ZoneId.of("Asia/Shanghai");
        ZoneId of = ZoneId.of("UTC-4");
        ZonedDateTime zonedDateTime = time.atZone(of2).withZoneSameInstant(of);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String format = zonedDateTime.format(dtf);
        return format;
    }

    public static void main(String[] args) {
        LocalDateTime localDateTime = parseLocalDateTime("2023-08-23 21:48:57");
        LocalDateTime localDateTime1 = parseLocalDateTime("2023-08-19 17:10:00");
        String s = americaCharge(localDateTime);
        String s1 = americaCharge(localDateTime1);
        System.out.println(s);
        System.out.println(localDateTime.plusSeconds(1));
    }
}
