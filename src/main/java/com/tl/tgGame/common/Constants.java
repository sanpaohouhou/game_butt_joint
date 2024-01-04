package com.tl.tgGame.common;


import java.time.format.DateTimeFormatter;


public class Constants {
    /**
     * 手机号码简单正则
     */
    public static final String PHONE_VERIFY_REGEX = "^1\\d{10}$";
    /**
     * 账号/密码: 英文加数字正则
     */
    public static final String ALPHANUMERIC_REGEX = "^[A-Za-z0-9]{6,20}$";

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static final DateTimeFormatter DATE_TIME_FORMAT_FRACTION = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    public static final DateTimeFormatter STANDARD_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static final DateTimeFormatter STANDARD_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static DateTimeFormatter DATA_FORMAT_COMPACT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static DateTimeFormatter NORMAL_DATE_TIME_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static DateTimeFormatter NORMAL_DATE_TIME_WITH_ZONE_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");

    public static DateTimeFormatter DATA_FORMAT_MINUTE_COMPACT = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    public static DateTimeFormatter UTC_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");


}
