package com.aliencat.javabase.java8.date;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class DateAndTime {

    //1.获取今天的日期
    @Test
    public void getCurrentDate(){
        LocalDate today = LocalDate.now();
        System.out.println("Java8's Local date : " + today);

        //这个是老版本
        Date date = new Date();
        System.out.println("Old version date : "+date);
    }

    //获取年月日信息
    @Test
    public void getDetailDate(){
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();

        System.out.printf("Year : %d  \nMonth : %d  \nday : %d ", year, month, day);
    }

    //处理特定日期
    @Test
    public void handleSpecilDate(){
        LocalDate dateOfBirth = LocalDate.of(2020, 01, 23);
        System.out.println("The specil date is : " + dateOfBirth);
    }

    //日期比较
    @Test
    public void compareDate(){
        LocalDate today = LocalDate.now();
        LocalDate date1 = LocalDate.of(2020, 01, 23);

        if(date1.equals(today)){
            System.out.printf("TODAY %s and DATE1 %s are same date %n", today, date1);
        }else{
            System.out.printf("TODAY %s and DATE1 %s are not same date %n", today, date1);
        }
    }

    //处理周期性日期
    @Test
    public void cycleDate(){
        LocalDate today = LocalDate.now();
        LocalDate dateOfBirth = LocalDate.of(2020, 01, 23);

        //某月某日
        MonthDay birthday = MonthDay.of(dateOfBirth.getMonth(), dateOfBirth.getDayOfMonth());
        MonthDay currentMonthDay = MonthDay.from(today);

        if(currentMonthDay.equals(birthday)){
            System.out.println("Many Many happy returns of the day !!");
        }else{
            System.out.println("Sorry, today is not your birthday");
        }
    }

    //获取当前时间，精确到毫秒
    @Test
    public void getCurrentTime(){
        LocalTime time = LocalTime.now();
        System.out.println("local time now : " + time);
    }

    //时间按小时推迟或者提前
    @Test
    public void plusHours(){
        LocalTime time = LocalTime.now();
        LocalTime newTime = time.plusHours(2); // 增加两小时
        System.out.println("Time after 2 hours : " +  newTime);

        newTime = time.plusHours(-2);// 减少两小时
        //newTime = time.minusHours(2);//当然也可以用minusHours方法实现上行代码同样效果
        System.out.println("Time before 2 hours : " +  newTime);
    }

    //时间按周推迟或者提前
    @Test
    public void nextWeek(){
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plus(1, ChronoUnit.WEEKS);
        System.out.println("Today is : " + today);
        System.out.println("Date after 1 week : " + nextWeek);
    }

    //时间按年推迟或者提前
    @Test
    public void minusDate(){
        LocalDate today = LocalDate.now();
        LocalDate previousYear = today.minus(1, ChronoUnit.YEARS);
        System.out.println("Date before 1 year : " + previousYear);

        LocalDate nextYear = today.plus(1, ChronoUnit.YEARS);
        System.out.println("Date after 1 year : " + nextYear);
    }

    //获取时钟
    @Test
    public void clock(){
        // 根据系统时间返回当前时间并设置为UTC。
        Clock clock = Clock.systemUTC();
        System.out.println("Clock : " + clock);

        // 根据系统时钟区域返回时间
        Clock defaultClock = Clock.systemDefaultZone();
        System.out.println("Clock : " + clock);
    }

    //判断日期是早于还是晚于另一个日期
    @Test
    public void isBeforeOrIsAfter(){
        LocalDate today = LocalDate.now();

        LocalDate tomorrow = LocalDate.of(2020, 1, 29);
        if(tomorrow.isAfter(today)){
            System.out.println("Tomorrow comes after today");
        }

        LocalDate yesterday = today.minus(1, ChronoUnit.DAYS);

        if(yesterday.isBefore(today)){
            System.out.println("Yesterday is day before today");
        }
    }

    //时区处理
    @Test
    public void getZoneTime(){
        //设置时区
        ZoneId america = ZoneId.of("America/New_York");

        LocalDateTime localtDateAndTime = LocalDateTime.now();

        ZonedDateTime dateAndTimeInNewYork  = ZonedDateTime.of(localtDateAndTime, america );
        System.out.println("现在的日期和时间在特定的时区 : " + dateAndTimeInNewYork);
    }

    //使用YearMonth处理特定日期
    @Test
    public void checkCardExpiry(){
        YearMonth currentYearMonth = YearMonth.now();
        System.out.printf("Days in month year %s: %d%n", currentYearMonth, currentYearMonth.lengthOfMonth());

        YearMonth creditCardExpiry = YearMonth.of(2028, Month.FEBRUARY);
        System.out.printf("Your credit card expires on %s %n", creditCardExpiry);
    }

    //判断闰年
    @Test
    public void isLeapYear(){
        LocalDate today = LocalDate.now();
        if(today.isLeapYear()){
            System.out.println("This year is Leap year");
        }else {
            System.out.println("2020 is not a Leap year");
        }
    }

    //计算两个日期间的间隔天数
    @Test
    public void calcDateDays(){
        LocalDate today = LocalDate.now();

        LocalDate java8Release = LocalDate.of(2020, Month.MAY, 14);

        Period periodToNextJavaRelease = Period.between(today, java8Release);

        System.out.println("Months left between today and Java 8 release : "
                + periodToNextJavaRelease.getMonths() );
    }

    //时差处理
    @Test
    public void ZoneOffset(){
        LocalDateTime datetime = LocalDateTime.of(2020, Month.FEBRUARY, 14, 19, 30);
        ZoneOffset offset = ZoneOffset.of("+05:30");
        OffsetDateTime date = OffsetDateTime.of(datetime, offset);
        System.out.println("Date and Time with timezone offset in Java : " + date);
    }

    //获取时间戳
    @Test
    public void getTimestamp(){
        Instant timestamp = Instant.now();
        System.out.println("What's the value of this instant : " + timestamp);
    }

    //使用预定义格式解析或格式化日期字符串
    @Test
    public void formateDate(){
        String dayAfterTommorrow = "20200123";
        LocalDate formatted = LocalDate.parse(dayAfterTommorrow, DateTimeFormatter.BASIC_ISO_DATE);
        System.out.printf("Date generated from String %s is %s %n", dayAfterTommorrow, formatted);

        LocalDate localDate = LocalDate.parse("2022-11-11");
        String date = localDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
        System.out.println(date); //十一月 11, 2022

    }

    @Test
    public void testCalendar(){
        //获取当前日期
        Date date = new Date();
        //将时间格式化成yyyy-MM-dd HH:mm:ss的格式
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //创建Calendar实例
        Calendar cal = Calendar.getInstance();
        //设置当前时间
        cal.setTime(date);
        //在当前时间基础上减一年
        cal.add(Calendar.YEAR, -1);
        System.out.println(format.format(cal.getTime()));
        //在当前时间基础上减一月
        cal.add(Calendar.MONTH,-1);
        System.out.println(format.format(cal.getTime()));
        //同理增加一天的方法：
        cal.add(Calendar.DATE, 1);
        System.out.println(format.format(cal.getTime()));
    }

    @Test
    public void testInstant(){
        System.out.println(LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC).getEpochSecond());
        System.out.println(LocalDateTime.now().toInstant(ZoneOffset.UTC));
    }



    // 获得某天最大时间 YYYY-MM-DD 23:59:59
    public static Date getEndOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());;
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    // 获得某天最小时间 YYYY-MM-DD 00:00:00
    public static Date getStartOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    @Test
    public  void getStartOfDay() {
        Date time = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        System.out.println(calendar.getTime().getTime());
        System.out.println(calendar.getTimeInMillis());
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime()));

        System.out.println(getStartOfDay(time));
    }

    @Test
    public  void getEndOfDay() {
        Date time = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        System.out.println(calendar.getTimeInMillis());
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime()));

        System.out.println(getEndOfDay(time));
    }

}
