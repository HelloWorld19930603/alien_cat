package com.aliencat.javabase.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjusters;

public class DateUtil {

    /**
     * 标准日期格式
     */
    public static final DateTimeFormatter FORMAT_STANDARD = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 中国标准时区
     */
    public static final ZoneId ZONEID_CHINA = ZoneId.of("Asia/Shanghai");

    /**
     * 当前时间
     *
     * @return LocalDateTime
     */
    public static LocalDateTime current() {
        return ZonedDateTime.now(ZONEID_CHINA).toLocalDateTime();
    }

    /**
     * 字符串时间转LocalDateTime
     *
     * @param strDate
     * @return LocalDateTime
     */
    public static LocalDateTime parse(String strDate) {
        return LocalDateTime.parse(strDate, FORMAT_STANDARD);
    }

    /**
     * 当前时间
     *
     * @return String
     */
    public static String now() {
        return current().format(FORMAT_STANDARD);
    }

    /**
     * localDateTime转字符串
     *
     * @param localDateTime
     * @return
     */
    public static String format(LocalDateTime localDateTime) {
        return localDateTime.format(FORMAT_STANDARD);
    }

    /**
     * 当前时间移动months个月后的时间
     *
     * @param months
     * @return String
     */
    public static String nowMoveMonths(long months) {
        return current().plusMonths(months).format(FORMAT_STANDARD);
    }

    /**
     * 当前时间移动days天后的时间
     *
     * @param days
     * @return String
     */
    public static String nowMoveDays(long days) {
        return current().plusDays(days).format(FORMAT_STANDARD);
    }

    /**
     * 当前时间移动hours小时后的时间
     *
     * @param hours
     * @return String
     */
    public static String nowMoveHours(long hours) {
        return current().plusHours(hours).format(FORMAT_STANDARD);
    }

    /**
     * 当前时间移动minutes分钟后的时间
     *
     * @param minutes
     * @return String
     */
    public static String nowMoveMinutes(long minutes) {
        return current().plusMinutes(minutes).format(FORMAT_STANDARD);
    }

    /**
     * 提取当前时间的日期部分
     *
     * @return
     */
    public static String toLocalDate() {
        return current().toLocalDate().toString();
    }

    /**
     * 提取当前时间的时间部分
     *
     * @return
     */
    public static String toLocalTime() {
        return current().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    /**
     * 求两个时间相差几年
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static long yearsDiff(String startDate, String endDate) {
        return yearsDiff(LocalDateTime.parse(startDate, FORMAT_STANDARD),
                LocalDateTime.parse(endDate, FORMAT_STANDARD));
    }

    public static long yearsDiff(Temporal startDate, Temporal endDate) {
        return ChronoUnit.YEARS.between(startDate, endDate);
    }

    /**
     * 求两个时间相差几个月
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static long monthsDiff(String startDate, String endDate) {
        return monthsDiff(parse(startDate), parse(endDate));
    }

    public static long monthsDiff(Temporal startDate, Temporal endDate) {
        return ChronoUnit.MONTHS.between(startDate, endDate);
    }

    /**
     * 求两个时间相差几天
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static long daysDiff(String startDate, String endDate) {
        return daysDiff(parse(startDate), parse(endDate));
    }

    public static long daysDiff(Temporal startDate, Temporal endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    /**
     * 求两个时间相差几个小时
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static long hoursDiff(String startDate, String endDate) {
        return hoursDiff(parse(startDate), parse(endDate));
    }

    public static long hoursDiff(Temporal startDate, Temporal endDate) {
        return ChronoUnit.HOURS.between(startDate, endDate);
    }

    /**
     * 求两个时间相差几分钟
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static long minutesDiff(String startDate, String endDate) {
        return minutesDiff(parse(startDate), parse(endDate));
    }

    public static long minutesDiff(Temporal startDate, Temporal endDate) {
        return ChronoUnit.MINUTES.between(startDate, endDate);
    }

    /**
     * 求两个时间相差几秒
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static long secondsDiff(String startDate, String endDate) {
        return secondsDiff(parse(startDate), parse(endDate));
    }

    public static long secondsDiff(Temporal startDate, Temporal endDate) {
        return ChronoUnit.SECONDS.between(startDate, endDate);
    }

    /**
     * 求两个时间相差多少毫秒
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static long millisDiff(String startDate, String endDate) {
        return millisDiff(parse(startDate), parse(endDate));
    }

    public static long millisDiff(Temporal startDate, Temporal endDate) {
        return ChronoUnit.MILLIS.between(startDate, endDate);
    }

    /**
     * 获取当月的第一天
     *
     * @return
     */
    public static String firstDayOfMonth() {
        return firstDayOfMonth(current().toLocalDate());
    }

    public static String firstDayOfMonth(LocalDate localDate) {
        return localDate.with(TemporalAdjusters.firstDayOfMonth()).toString();
    }

    /**
     * 获取当月的最后一天
     *
     * @return
     */
    public static String lastDayOfMonth() {
        return lastDayOfMonth(current().toLocalDate());
    }

    public static String lastDayOfMonth(LocalDate localDate) {
        return localDate.with(TemporalAdjusters.lastDayOfMonth()).toString();
    }

}