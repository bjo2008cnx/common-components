package com.github.common.components.util.lang;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {
    public static final String formatPattern = "yyyy-MM-dd";

    public static final String formatPattern_Short = "yyyyMMdd";

    public static final String formatPattern_chinese = "yyyy年MM月dd日";

    public static final String FORMAT_PATTERN_MEDIUM = "yyyy-MM-dd HH:mm:ss";

    public static Date strToDate(String time) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Date d = sdf.parse(time);

        return d;
    }

    public static Date strToDate(String time, String format) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat(format);

        Date d = sdf.parse(time);

        return d;
    }

    public static Date strToTime(String time) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd:HH");

        Date d = sdf.parse(time);

        return d;
    }

    /**
     * 生成当前时间对应的包含小时的时间字符串
     *
     * @return String 时间字符串
     */
    public static String getHourStr() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
        return sdf.format(new Date());
    }

    /**
     * 生成java.util.Date类型的对象
     *
     * @param year  int 年
     * @param month int 月
     * @param day   int 日
     * @return Date java.util.Date类型的对象
     */
    public static Date getDate(int year, int month, int day) {
        GregorianCalendar d = new GregorianCalendar(year, month - 1, day);
        return d.getTime();
    }

    public static Date getDate(int yyyyMMdd) {
        int dd = yyyyMMdd % 100;
        int yyyyMM = yyyyMMdd / 100;
        int mm = yyyyMM % 100;
        int yyyy = yyyyMM / 100;
        GregorianCalendar d = new GregorianCalendar(yyyy, mm - 1, dd);
        return d.getTime();
    }

    /**
     * 生成java.util.Date类型的对象
     *
     * @param year  int 年
     * @param month int 月
     * @param day   int 日
     * @param hour  int 小时
     * @return Date java.util.Date对象
     */
    public static Date getDate(int year, int month, int day, int hour) {
        GregorianCalendar d = new GregorianCalendar(year, month - 1, day, hour, 0);
        return d.getTime();
    }

    /**
     * 生成圆整至小时的当前时间 例如：若当前时间为（2004-08-01 11:30:58），将获得（2004-08-01 11:00:00）的日期对象
     *
     * @return Date java.util.Date对象
     */
    public static Date getRoundedHourCurDate() {

        Calendar cal = GregorianCalendar.getInstance();

        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        return cal.getTime();

    }

    public static Date getThisYearLastDate() {
        Calendar cal = GregorianCalendar.getInstance();
        GregorianCalendar d = new GregorianCalendar(cal.get(Calendar.YEAR), 11, 31, 23, 59, 59);
        return d.getTime();
    }

    /**
     * 生成当天零时的日期对象 例如：若当前时间为（2004-08-01 11:30:58），将获得（2004-08-01 00:00:00）的日期对象
     *
     * @return Date java.util.Date对象
     */
    public static Date getRoundedDayCurDate() {
        Calendar cal = new GregorianCalendar();

        return new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).getTime();
    }

    /**
     * 生成圆整至小时的当前时间 例如：若给定时间为（2004-08-01 11:30:58），将获得（2004-08-01 11:00:00）的日期对象
     *
     * @param dt Date java.util.Date对象
     * @return Date java.util.Date对象
     */
    public static Date getRoundedHourDate(Date dt) {

        Calendar cal = new GregorianCalendar();

        cal.setTime(dt);

        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        return cal.getTime();
    }

    /**
     * 生成给定时间零时的日期对象 例如：若给定时间为（2004-08-01 11:30:58），将获得（2004-08-01 00:00:00）的日期对象
     *
     * @return Date java.util.Date对象
     */
    public static Date getRoundedDayDate(Date dt) {
        Calendar cal = new GregorianCalendar();

        cal.setTime(dt);

        return new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).getTime();
    }

    /**
     * 获得给定时间的第二天零时的日期对象 例如：若给定时间为（2004-08-01 11:30:58），将获得（2004-08-02
     * 00:00:00）的日期对象 若给定时间为（2004-08-31 11:30:58），将获得（2004-09-01 00:00:00）的日期对象
     *
     * @param dt Date 给定的java.util.Date对象
     * @return Date java.util.Date对象
     */
    public static Date getNextDay(Date dt) {

        Calendar cal = new GregorianCalendar();
        cal.setTime(dt);
        return new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 1).getTime();

    }

    /**
     * @param dt      Date 给定的java.util.Date对象
     * @param weekDay int 就是周几的”几“，周日是7
     * @return Date java.util.Date对象
     */
    public static Date getWeekDay(Date dt, int weekDay) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(dt);
        if (weekDay == 7) weekDay = 1;
        else weekDay++;
        cal.set(GregorianCalendar.DAY_OF_WEEK, weekDay);
        return cal.getTime();
    }

    /**
     * 获得给定时间的第N天零时的日期对象 例如：若给定时间为（2004-08-01 11:30:58），将获得（2004-08-02
     * 00:00:00）的日期对象 若给定时间为（2004-08-31 11:30:58），将获得（2004-09-01 00:00:00）的日期对象
     *
     * @param dt Date 给定的java.util.Date对象
     * @return Date java.util.Date对象
     */
    public static Date getNextDay(Date dt, Long n) {

        Calendar cal = new GregorianCalendar();
        cal.setTime(dt);

        return new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + n.intValue()).getTime();

    }

    public static Date getNextMonth(Date dt, Long n) {

        Calendar cal = new GregorianCalendar();
        cal.setTime(dt);

        Calendar firstCal = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + n.intValue(), 1);
        if (firstCal.getActualMaximum(Calendar.DAY_OF_MONTH) < cal.get(Calendar.DAY_OF_MONTH)) {
            return new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + n.intValue(), firstCal.getActualMaximum(Calendar.DAY_OF_MONTH)).getTime();
        } else {
            return new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + n.intValue(), cal.get(Calendar.DAY_OF_MONTH)).getTime();
        }

    }

    public static long getBetweenDate(Date startDate, Date endDate) {
        long startDateTime = startDate.getTime();
        long endDateTime = endDate.getTime();
        long dayTime = 24 * 60 * 60 * 1000;
        long days = (endDateTime - startDateTime) / dayTime;
        return days;
    }

    public static long getMonthLength(String countDate) throws ParseException {
        String firstDay = countDate.substring(0, countDate.length() - 2) + "01";
        Date startDate = strToDate(firstDay);
        Date endDate = getNextMonth(startDate, 1L);
        long startDateTime = startDate.getTime();
        long endDateTime = endDate.getTime();
        long dayTime = 24 * 60 * 60 * 1000;
        long days = (endDateTime - startDateTime) / dayTime;
        return days;
    }

    /**
     * 获得当前时间的第二天零时的日期对象 例如：若当前时间为（2004-08-01 11:30:58），将获得（2004-08-02
     * 00:00:00）的日期对象 若当前时间为（2004-08-31 11:30:58），将获得（2004-09-01 00:00:00）的日期对象
     *
     * @return Date java.util.Date对象
     */
    public static Date getNextDay() {

        Calendar cal = GregorianCalendar.getInstance();
        return new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 1).getTime();

    }

    /**
     * 获取days天之前的日期，若当前日期为（2004-08-01 11:30:58），传入1，将获得（2004-07-31
     * 00:00:00）的日期对象
     *
     * @param days
     * @return
     */
    public static final Date getNextDays(int days) {
        Calendar cal = GregorianCalendar.getInstance();
        return new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) - days).getTime();
    }

    /**
     * 获取hours小时之前的日期，若当前日期为（2004-08-01 11:30:58），传入1，将获得（2004-08-01
     * 10:30:58）的日期对象
     *
     * @return
     */
    public static final Date getLastDateByHours(int hours) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.add(Calendar.HOUR, -hours);
        return cal.getTime();
    }

    /**
     * 获取hours小时之前的日期，若date为（2004-08-01 11:30:58），传入1，将获得（2004-08-01
     * 10:30:58）的日期对象
     *
     * @return
     */
    public static final Date getLastDateByHours(Date date, int hours) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, -hours);

        return cal.getTime();
    }

    /**
     * 获取mins分钟之前的日期，若当前日期为（2004-08-01 11:30:58），传入1，将获得（2004-08-01
     * 11:29:58）的日期对象
     *
     * @return
     */
    public static final Date getLastDateByMins(int mins) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.add(Calendar.MINUTE, -mins);
        return cal.getTime();
    }

    /**
     * 获取mins分钟之前的日期，若date为（2004-08-01 11:30:58），传入1，将获得（2004-08-01
     * 11:29:58）的日期对象
     *
     * @return
     */
    public static final Date getLastDateByMins(Date date, int mins) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, -mins);
        return cal.getTime();
    }

    /**
     * 将java.util.Date类型的对象转换为java.sql.Timestamp类型的对象
     *
     * @param dt Date
     * @return Timestamp
     */
    public static java.sql.Timestamp convertSqlDate(Date dt) {
        if (dt == null) {
            return new java.sql.Timestamp(0);
        }
        return new java.sql.Timestamp(dt.getTime());
    }

    /**
     * 格式化当前时间，返回如：2004年8月1日形式的字符串
     *
     * @return String
     */
    public static String formatCurrrentDate() {
        Date pdate = new Date();
        return formatDate(pdate, "yyyyMMdd");
    }

    /**
     * 按照给定格式返回代表日期的字符串
     *
     * @param pDate  Date
     * @param format String 日期格式
     * @return String 代表日期的字符串
     */
    public static String formatDate(Date pDate, String format) {

        if (pDate == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(pDate);
    }

    /**
     * 返回给定时间的小时数 例如：时间（2004-08-01 3:12:23）将返回 03 时间（2004-08-01 19:12:23）将返回19
     *
     * @param pDate Date 给定时间
     * @return String 代表小时数的字符串
     */
    public static String getHour(Date pDate) {
        return formatDate(pDate, "HH");
    }

    /**
     * 获得上一个月的最后一天
     *
     * @return
     */
    public static Calendar getTheLastDayOfTheMonth(int year, int month) {
        Calendar cal = new GregorianCalendar();
        cal.set(year, month, 1);
        return new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) - 1);

    }

    /**
     * 验证字符串是不是合法的日期；严格判断日期格式YYYYMMDD的正则表达式：包括闰年的判断、大月小月的判断
     *
     * @param dateString 待验证的日期字符串
     * @return 满足则返回true，不满足则返回false
     * @author zhangpeng mrd3.4.0
     */
    public static boolean validateDateString(String dateString) {

        if (dateString == null || dateString.equals("")) {
            return false;
        }

        // 日期格式YYYYMMDD的正则表达式,世纪年为闰年，如2000
        String regDate = "^(((([02468][048])|([13579][26]))[0]{2})(02)(([0][1-9])|([1-2][0-9])))" +
                // 世纪年不为闰年如2100
                "|(((([02468][1235679])|([13579][01345789]))[0]{2})(02)(([0][1-9])|([1][0-9])|([2][0-8])))" +
                // 非世纪年为闰年，如1996
                "|(([0-9]{2}(([0][48])|([2468][048])|([13579][26])))(02)(([0][1-9])|([1-2][0-9])))" +
                // 非世纪年不为闰年，如1997
                "|(([0-9]{2}(([02468][1235679])|([13579][01345789])))(02)(([0][1-9])|([1][0-9])|([2][0-8])))" +
                // 大月，有31天
                "|(([0-9]{4})(([0]{1}(1|3|5|7|8))|10|12)(([0][1-9])|([1-2][0-9])|30|31))" +
                // 小月，只有30天
                "|(([0-9]{4})(([0]{1}(4|6|9))|11)(([0][1-9])|([1-2][0-9])|30))$";

        return dateString.matches(regDate);
    }

    /**
     * 验证字符串是不是合法的日期；严格判断日期格式YYYYMMDD的正则表达式：包括闰年的判断、大月小月的判断
     *
     * @param dateString 待验证的日期字符串
     * @return 满足则返回true，不满足则返回false
     * @author wangl 20090401
     */
    public static boolean validateDateString(String dateString, String format) {

        if (dateString == null || dateString.equals("")) {
            return false;
        }
        if (format == null || format.equals("")) {
            return false;
        }
        Date date = null;
        SimpleDateFormat df = new SimpleDateFormat(format);
        df.setLenient(false);// 这个的功能是不把2008-13-3 转换为2009-1-3
        try {
            date = df.parse(dateString);
        } catch (Exception e) {
            // d=new Date();
            // System.out.println("你输入的日期不合法，请重新输入");
            return false;
        }
        // String sdata=df.format(d);
        // System.out.println(sdata);
        return true;
    }

    public static void main(String[] args) throws ParseException {
        // Calendar cal = new GregorianCalendar();
        // cal.set(2004, 8, 6);
        // Calendar cal1 = Calendar.getInstance();
        // cal1.set(2004, 8, 9);
        // System.out.println("cal1.getTime() = " + cal1.getTime());
        // LinkedHashMap r = getNullLogDateList(cal.getTime(), cal1.getTime(),
        // 2);
        // int i = 0;
        // for (Iterator iter = r.keySet().iterator(); iter.hasNext(); ) {
        // Object item = (Object) iter.next();
        // System.out.println("i++ = " + i++);
        // System.out.println("item = " + item);
        // }
        // Date d=new Date();
        // d=DateUtils.getNextDay(d,new Long(-1));
        // String date=DateUtils.formatDate(d,"yyyyMMdd");
        // System.out.println(date);

        // int year=2004;
        // for (int month=0;month<12;month++)
        // {
        // Calendar cal=DateUtils.getTheLastDayOfTheMonth(year, month);
        // Date d=cal.getTime();
        // System.out.println(DateUtils.formatDate(d,"yyyy��MM��" ));
        // Long mo=new
        // Long(cal.get(Calendar.YEAR)*100+cal.get(Calendar.MONTH)+1);
        // System.out.println(mo);
        // }

        // Date date = getDate(2006,1,31);
        Date date = getDate(20060131);
        // System.out.println("next month date:"+formatDate(getNextMonth(date,new
        // Long(-11)),"yyyyMMdd"));
        // System.out.println(DateUtils.formatDate(new Date(), "yyyyMMdd"));
        String registerTime = DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
        System.out.println(registerTime);

        // System.out.println("first day of week: "
        // +getDate(2005,8,1,4).getTime());
        int space = getMonthSpace(getDate(20150106), getDate(20151221));
        //int seconds=getSecondSpace(new Date(), dateEnd)
        System.out.println(space);
        System.out.println(getStartOfDate(new Date()));
        System.out.println("last day of month" + getMonthLast(11));
        System.out.println(getPrefixDate("1"));
    }

    /**
     * Compare Date by precision.
     *
     * @param date1
     * @param date2
     * @param precision
     * @return the same meaning as result of Date.compareTo(Date other)
     */
    public static int compareDate(final Date date1, final Date date2, int precision) {
        Calendar c = Calendar.getInstance();

        List<Integer> fields = new ArrayList<Integer>();
        fields.add(Calendar.YEAR);
        fields.add(Calendar.MONTH);
        fields.add(Calendar.DAY_OF_MONTH);
        fields.add(Calendar.MINUTE);
        fields.add(Calendar.SECOND);
        fields.add(Calendar.MILLISECOND);

        Date d1 = date1;
        Date d2 = date2;
        if (fields.contains(precision)) {
            c.setTime(date1);
            for (int i = 0; i < fields.size(); i++) {
                int field = (fields.get(i)).intValue();
                if (field > precision) c.set(field, 0);
            }
            d1 = c.getTime();
            c.setTime(date2);
            for (int i = 0; i < fields.size(); i++) {
                int field = (fields.get(i)).intValue();
                if (field > precision) c.set(field, 0);
            }
            d2 = c.getTime();
        }
        return d1.compareTo(d2);
    }


    /**
     * 获得给定时间的第N天零时的日期对象 例如：若给定时间为（2008-08-02 11:30:58）,n为1，将获得（2008-07-02
     * 00:00:00）的日期对象 若给定时间为（2008-09-01 11:30:58）,n为1，将获得（2008-08-01
     * 00:00:00）的日期对象
     *
     * @param dt Date 给定的java.util.Date对象
     * @return Date java.util.Date对象
     */
    public static Date getLastMonth(Date dt, Long n) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(dt);
        return new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) - n.intValue(), cal.get(Calendar.DAY_OF_MONTH)).getTime();
    }

    public static Date getDateFromStr(String str, String format) {
        if (StringUtil.isEmpty(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        if (StringUtil.isEmpty(str)) {
            return null;
        }
        java.text.DateFormat df = new SimpleDateFormat(format);
        try {
            return df.parse(str);
        } catch (ParseException e) {
            throw new IllegalArgumentException("根式不正确");
        }
    }

    public static boolean isDate(String str, String format) {
        if (StringUtil.isEmpty(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        if (StringUtil.isEmpty(str)) {
            return false;
        }
        java.text.DateFormat df = new SimpleDateFormat(format);
        try {
            df.parse(str);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * 将指定日期的时间改为18:00:00
     *
     * @return
     */
    public static Date getDateForEighteenHour(Date dt) {
        if (dt == null) {
            dt = new Date();
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(dt);
        cal.set(Calendar.HOUR_OF_DAY, 18);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * 计算时间偏移结果
     *
     * @param type      偏移的类别
     * @param iQuantity 偏移数量
     * @return 偏移后的时间串
     */
    public static Date getChangeDateAddTime(Date date, Date time, String type, int iQuantity) {
        int year = Integer.parseInt(DateUtil.formatDate(date, "yyyy"));
        int month = Integer.parseInt(DateUtil.formatDate(date, "MM"));
        // 月份修正,必须在日期变更前修改正月份，否则小月份都会产成问题
        month = month - 1;
        int day = Integer.parseInt(DateUtil.formatDate(date, "dd"));
        int hour = Integer.parseInt(DateUtil.formatDate(time, "HH"));
        int mi = Integer.parseInt(DateUtil.formatDate(time, "mm"));
        int ss = Integer.parseInt(DateUtil.formatDate(time, "ss"));
        GregorianCalendar gc = new GregorianCalendar(year, month, day, hour, mi, ss);

        if (iQuantity == 0 || type == null) return gc.getTime();
        // 月份修正,必须在日期变更前修改正月份，否则小月份都会产成问题
        // gc.add(GregorianCalendar.MONTH, -1);

        if (type.equalsIgnoreCase("y")) {
            gc.add(GregorianCalendar.YEAR, iQuantity);
        } else if (type.equalsIgnoreCase("m")) {
            gc.add(GregorianCalendar.MONTH, iQuantity);
        } else if (type.equalsIgnoreCase("d")) {
            gc.add(GregorianCalendar.DATE, iQuantity);
        } else if (type.equalsIgnoreCase("h")) {
            gc.add(GregorianCalendar.HOUR, iQuantity);
        } else if (type.equalsIgnoreCase("mi")) {
            gc.add(GregorianCalendar.MINUTE, iQuantity);
        } else if (type.equalsIgnoreCase("s")) {
            gc.add(GregorianCalendar.SECOND, iQuantity);
        }

        return gc.getTime();
    }


    /**
     * 获取上周周一
     *
     * @author modi
     * @version 1.0.0
     */
    public static String getLastMonday(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        c.add(Calendar.DATE, -7);
        String result = sdf.format(c.getTime());
        return result;
    }

    /**
     * 获取上上周周一
     *
     * @author modi
     * @version 1.0.0
     */
    public static String getTwoMonday(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        c.add(Calendar.DATE, -14);
        String result = sdf.format(c.getTime());
        return result;
    }

    /**
     * 获取三周前的周一
     *
     * @author modi
     * @version 1.0.0
     */
    public static String getThrMonday(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        c.add(Calendar.DATE, -21);
        String result = sdf.format(c.getTime());
        return result;
    }

    /**
     * 获取上周周日
     *
     * @author modi
     * @version 1.0.0
     */
    public static String getLastSunday(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        c.add(Calendar.DATE, -7);
        String result = sdf.format(c.getTime());
        return result;
    }

    /**
     * 获取上月一号
     *
     * @author modi
     * @version 1.0.0
     */
    public static String getPreMonthFirst(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DATE, 1);
        c.add(Calendar.MONTH, -1);
        String result = sdf.format(c.getTime());
        return result;
    }

    /**
     * 获取上月最后一天
     *
     * @author modi
     * @version 1.0.0
     */
    public static String getPreMonthLast(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DATE, 1);
        c.add(Calendar.DATE, -1);
        String result = sdf.format(c.getTime());
        return result;
    }

    /**
     * 获取上个季度的第一天
     *
     * @author modi
     * @version 1.0.0
     */
    public static String getThrMonthFirst(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH);

        if (month == Calendar.JANUARY || month == Calendar.FEBRUARY || month == Calendar.MARCH) {
            c.set(Calendar.MONTH, Calendar.OCTOBER);
            c.add(Calendar.YEAR, -1);
        } else if (month == Calendar.APRIL || month == Calendar.MAY || month == Calendar.JUNE) {
            c.set(Calendar.MONTH, Calendar.JANUARY);
        } else if (month == Calendar.JULY || month == Calendar.AUGUST || month == Calendar.SEPTEMBER) {
            c.set(Calendar.MONTH, Calendar.APRIL);
        } else {
            c.set(Calendar.MONTH, Calendar.JULY);
        }

        c.set(Calendar.DATE, 1);
        String result = sdf.format(c.getTime());
        return result;
    }

    /**
     * 获取上个季度的最后一天
     *
     * @author modi
     * @version 1.0.0
     */
    public static String getThrMonthLast(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH);

        if (month == Calendar.JANUARY || month == Calendar.FEBRUARY || month == Calendar.MARCH) {
            c.set(Calendar.MONTH, Calendar.JANUARY);
        } else if (month == Calendar.APRIL || month == Calendar.MAY || month == Calendar.JUNE) {
            c.set(Calendar.MONTH, Calendar.APRIL);
        } else if (month == Calendar.JULY || month == Calendar.AUGUST || month == Calendar.SEPTEMBER) {
            c.set(Calendar.MONTH, Calendar.JULY);
        } else {
            c.set(Calendar.MONTH, Calendar.OCTOBER);
        }

        c.set(Calendar.DATE, 1);
        c.add(Calendar.DATE, -1);
        String result = sdf.format(c.getTime());
        return result;
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static String getCurrentDate() {
        SimpleDateFormat format = new SimpleDateFormat(formatPattern);
        return format.format(new Date());
    }

    /**
     * 获取制定毫秒数之前的日期
     *
     * @param timeDiff
     * @return
     */
    public static String getDesignatedDate(long timeDiff) {
        SimpleDateFormat format = new SimpleDateFormat(formatPattern);
        long nowTime = System.currentTimeMillis();
        long designTime = nowTime - timeDiff;
        return format.format(designTime);
    }

    /**
     * 获取前几天的日期
     */
    public static String getPrefixDate(String count) {
        Calendar cal = Calendar.getInstance();
        int day = 0 - Integer.parseInt(count);
        cal.add(Calendar.DATE, day);   // int amount   代表天数
        Date datNew = cal.getTime();
        SimpleDateFormat format = new SimpleDateFormat(formatPattern);
        return format.format(datNew);
    }

    /**
     * 日期转换成字符串
     *
     * @param date
     * @return
     */
    public static String dateToString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(formatPattern);
        return format.format(date);
    }


    /**
     * @param date
     * @param formatPattern
     * @return
     */
    public static String dateToString(Date date, String formatPattern) {
        SimpleDateFormat format = new SimpleDateFormat(formatPattern);
        return format.format(date);

    }

    /**
     * 返回两个时间相差的月数
     *
     * @param dateBegin 开始时间
     * @param dateEnd   结束时间
     *                  eg:  2016-02-03 2015-12-01
     * @return
     */
    public static int getMonthSpace(Date dateBegin, Date dateEnd) {
        try {
            long startDateTime = dateBegin.getTime();
            long endDateTime = dateEnd.getTime();
            long dayTime = 24 * 60 * 60 * 1000;
            double days = Math.abs((endDateTime - startDateTime)) / dayTime;
            int month = (int) Math.ceil(days / 30);

            return month;
        } catch (Exception e) {
            return Integer.MAX_VALUE;
        }

    }

    /**
     * 返回两个日期相差的秒数
     *
     * @param dateBegin
     * @param dateEnd
     * @return
     */
    public static int getSecondSpace(Date dateBegin, Date dateEnd) {

        long startDateTime = dateBegin.getTime();
        long endDateTime = dateEnd.getTime();
        long secondTime = 1000;
        double seconds = Math.abs((endDateTime - startDateTime)) / secondTime;
        int result = (int) Math.ceil(seconds);

        return result;
    }


    /**
     * 返回date凌晨零点的时间
     *
     * @param date
     * @return
     */
    public static Date getStartOfDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.clear(Calendar.AM_PM);
        calendar.clear(Calendar.HOUR);
        calendar.clear(Calendar.HOUR_OF_DAY);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        return calendar.getTime();
    }

    /**
     * 返回date后一天凌晨零点的时间
     *
     * @param date
     * @return
     */
    public static Date getStartOfNextDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.clear(Calendar.AM_PM);
        calendar.clear(Calendar.HOUR);
        calendar.clear(Calendar.HOUR_OF_DAY);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 获得剩余时间
     * 如果剩余时间为0，返回 空
     * 如：当前时间“2016-01-25 14:58:55”，截止时间“2016-01-25 15:10:05”，返回“11分10秒”
     *
     * @param deadDate
     * @return
     */
    public static String getLeftTimeString(Date deadDate) {
        Date currentDate = new Date();
        //剩余秒
        long diff = (deadDate.getTime() - currentDate.getTime()) / 1000;
        if (diff <= 0) {
            return "";
        }

        long second = diff % 60;

        long minute = (diff / 60) % 60;

        long hour = (diff / (60 * 60)) % 24;

        long day = diff / (24 * 3600);

        StringBuilder builder = new StringBuilder();
        if (day > 0) {
            builder.append(day);
            builder.append("天");
        }
        if (hour > 0) {
            builder.append(hour);
            builder.append("小时");
        }
        if (minute > 0) {
            builder.append(minute);
            builder.append("分");
        }

        builder.append(second);
        builder.append("秒");
        return builder.toString();
    }

    /**
     * 获取当年的最后一天
     *
     * @return
     */
    public static Date getCurrentYearLast() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        return getYearLast(currentYear);
    }

    /**
     * 获取某年的最后一天
     *
     * @param year
     * @return
     */
    public static Date getYearLast(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date date = calendar.getTime();
        return date;
    }

    /**
     * 获取当年某月的最后一天
     *
     * @param month
     * @return
     */
    public static Date getMonthLast(int month) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.roll(Calendar.DAY_OF_MONTH, -1);
        Date date = calendar.getTime();
        return date;
    }

    /**
     * 根据时间长度获得时间
     *
     * @param date
     * @return
     */
    public static String getDate(Date date, int length) {
        DateFormat dateFormat = null;
        if (length == 6) {
            dateFormat = DateFormat.YYMMDD;
        } else if (length == 8) {
            dateFormat = DateFormat.YYYYMMDD;
        } else if (length == 14) {
            dateFormat = DateFormat.YYYYMMDDHHMMSS;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat.getFormat());
        return simpleDateFormat.format(date);
    }

    /**
     * 根据时间长度获得时间
     *
     * @param length
     * @return
     */
    public static String getCurrentDate(int length) {
        return getDate(new Date(), length);
    }

    public static Date getDateAfterSeconds(Date date, int seconds) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, seconds);
        return cal.getTime();
    }
}
