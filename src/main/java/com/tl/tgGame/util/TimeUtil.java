package com.tl.tgGame.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtil {

    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
        return instant.atZone(zoneId).toLocalDateTime();
    }


    public static LocalDateTime parseTimestamp(String timestamp){
        Instant instant = Instant.ofEpochMilli(Long.parseLong(timestamp));
        ZoneId zoneId = ZoneId.systemDefault();
        String format = LocalDateTime.ofInstant(instant, zoneId).format(dateTimeFormatter);
        return LocalDateTime.parse(format,dateTimeFormatter);
    }
    /**
     * 转换成毫秒级时间戳
     * @param now
     * @return
     */
    public static Long getTimestamp(LocalDateTime now) {
        return now.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    }

    /**
     * 转换成秒级时间戳
     * @param now
     * @return
     */
    public static Long nowTimeStamp(LocalDateTime now) {
        return now.toInstant(ZoneOffset.ofHours(8)).getEpochSecond();
    }

    /**
     * LocalDateTime 转换 美东时间 uct-4
     */
    public static String americaCharge(LocalDateTime time) {
        ZoneId of2 = ZoneId.of("Asia/Shanghai");
        ZoneId of = ZoneId.of("UTC-4");
        ZonedDateTime zonedDateTime = time.atZone(of2).withZoneSameInstant(of);
        return zonedDateTime.format(dateTimeFormatter);
    }

    /**
     * 美东时间转换北京时区
     *
     * @param time
     * @param
     * @return
     */
    public static LocalDateTime shanghaiCharge(LocalDateTime time) {
        ZonedDateTime of = ZonedDateTime.of(time, ZoneId.of("America/New_York"));
        ZonedDateTime zonedDateTime = of.withZoneSameInstant(ZoneId.of("Asia/Shanghai"));
        String format = zonedDateTime.format(dateTimeFormatter);
        return parseLocalDateTime(format);
    }

    /**
     * 获取当前时间.time就传0
     *
     * @param time
     * @return
     */
    public static Long nowCalendar(int time, int calendarId) {
        Calendar calendar = Calendar.getInstance();
        // 将时间往前推15分钟
        calendar.add(calendarId, time);
        return calendar.getTimeInMillis();
    }

    public static Long nowCalendar() {
        return nowCalendar(0, Calendar.MINUTE);
    }

    public static LocalDateTime getStringDisplayLocalDateTime(String time) {
        return LocalDateTime.parse(time, dateTimeFormatter);
    }

    public static void main(String[] args) {
        String str = "2023-09-03 23:08:58";
        LocalDateTime localDateTime = parseLocalDateTime(str);
        LocalDateTime localDateTime1 = shanghaiCharge(localDateTime);
        LocalDateTime stringDisplayLocalDateTime = parseTimestamp("1694080792493");
        System.out.println(localDateTime1);
        System.out.println(stringDisplayLocalDateTime);
    }

}
