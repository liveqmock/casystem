package ca.core.logic;

import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.sql.Timestamp;
import java.util.GregorianCalendar;

/**
 * Created with IntelliJ IDEA.
 * User: SeleneFox
 * Date: 13-4-28
 * Time: 下午4:11
 * To change this template use File | Settings | File Templates.
 */
public class DateUtil {
    private static Logger logger = Logger.getLogger(DateUtil.class);
    /**
     * 默认的日期格式
     */
    public static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static String DEFAULT_TIME_FORMAT = "HH:mm:ss";
    public static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static String DATE_YEAR_MONTH = "yyyy-MM";
    public static String DATE_YEAR_MONTH_DATE = "yyyyMMdd";

    /**
     * 取得当前日期
     *
     * @return Date 当前日期
     */
    public static Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * 返回当前日期对应的默认格式的字符串
     *
     * @return String 当前日期对应的字符串
     */
    public static String getCurrentStringDate() {
        return convertDate2String(getCurrentDate(), DEFAULT_DATE_FORMAT);
    }
    /**
     * 返回当前日期对应的默认格式的字符串
     *
     * @return Timestamp
     */
    public static Timestamp getCurrentStringTimestamp() {
        return Timestamp.valueOf(convertDate2String(getCurrentDate(), DATE_FORMAT));
    }
    /**
     * 返回当前日期对应的默认格式的字符串
     *
     * @return Timestamp
     */
    public static Timestamp getStringTimestamp(Date dt) {
        return Timestamp.valueOf(convertDate2String(dt, DATE_FORMAT));
    }



    /**
     * 返回当前日期对应的指定格式的字符串
     *
     * @param dateFormat - 日期格式
     * @return String 当前日期对应的字符串`
     */
    public static String getCurrentStringDate(String dateFormat) {
        return convertDate2String(getCurrentDate(), dateFormat);
    }

    /**
     * 将日期转换成指定格式的字符串
     *
     * @param date       - 要转换的日期
     * @param dateFormat - 日期格式
     * @return String 日期对应的字符串
     */
    public static String convertDate2String(Date date, String dateFormat) {
        SimpleDateFormat sdf;
        if (dateFormat != null && !dateFormat.equals("")) {
            try {
                sdf = new SimpleDateFormat(dateFormat);
            } catch (Exception e) {
                logger.error(e);
                sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
            }
        } else {
            sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        }
        if (date != null) {
            return sdf.format(date);
        }
        return null;
    }

    /**
     * 从java.sql.Date 转换到 java.util.Date
     *
     * @param date java.sql.Date类型的时间对象
     * @return java.util.Date类型的时间对象
     */
    public static Date converDate2JavaDate(java.sql.Date date) {
        if (date != null) {
            return new Date(date.getTime());
        }
        return null;
    }

    /**
     * 从java.util.Date转换到java.sql.Date
     *
     * @param date java.util.Date类型的时间对象
     * @return java.sql.Date类型的时间对象
     */
    public static java.sql.Date converDate2SqlDate(Date date) {
        if (date != null) {
            return new java.sql.Date(date.getTime());
        }
        return null;
    }

    /**
     * 将字符串转换成日期
     *
     * @param stringDate - 要转换的字符串格式的日期
     * @return Date 字符串对应的日期
     */
    public static Date convertString2Date(String stringDate) {
        return convertString2Date(stringDate, DEFAULT_DATE_FORMAT);
    }

    /**
     * 将字符串转换成日期
     *
     * @param stringDate - 要转换的字符串格式的日期
     * @param dateFormat - 要转换的字符串对应的日期格式
     * @return Date 字符串对应的日期
     */
    public static Date convertString2Date(String stringDate, String dateFormat) {
        SimpleDateFormat sdf;
        if (dateFormat != null && !dateFormat.equals("")) {
            try {
                sdf = new SimpleDateFormat(dateFormat);
            } catch (Exception e) {
                logger.error(e);
                sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
            }
        } else {
            sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        }
        try {
            return sdf.parse(stringDate);
        } catch (ParseException pe) {
            return new Date();
        }
    }

    /**
     * 将一种格式的日期字符串转换成默认格式的日期字符串
     *
     * @param oldStringDate - 要格式化的日期字符串
     * @param oldFormat     - 要格式化的日期的格式
     * @return String 格式化后的日期字符串
     */
    public static String formatStringDate(String oldStringDate, String oldFormat) {
        return convertDate2String(convertString2Date(oldStringDate, oldFormat), DEFAULT_DATE_FORMAT);
    }

    /**
     * 将一种格式的日期字符串转换成另一种格式的日期字符串
     *
     * @param oldStringDate - 要格式化的日期字符串
     * @param oldFormat     - 要格式化的日期的格式
     * @param newFormat     - 格式化后的日期的格式
     * @return String 格式化后的日期字符串
     */
    public static String formatStringDate(String oldStringDate, String oldFormat, String newFormat) {
        return convertDate2String(convertString2Date(oldStringDate, oldFormat), newFormat);
    }


    public static int getDaysByMonth(int year, int month) {
        Calendar c = new GregorianCalendar(year, month, 1);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static int getNowyear() {
        return Integer.valueOf(convertDate2String(getCurrentDate(), "yyyy"));
    }

    public static int compDate(Date date1, Date date2) {
        Calendar c = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c.clear();
        c.setTime(date1);
        c2.clear();
        c2.setTime(date2);
        return c.compareTo(c2);
    }

    /**
     * 得到 T 天 前的日期
     *
     * @param t 天数
     * @return 日期
     */
    public String getDateString(int t) {
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, -1 * t);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        return df.format(monday) + " 00:00:00";
    }

    public static String getFormatDate(String time){
        if(null!=time && !"".equals(time)){
            return time.substring(0,time.lastIndexOf("."));
        }
        return "";
    }
}
