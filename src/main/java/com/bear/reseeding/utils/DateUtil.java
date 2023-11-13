package com.bear.reseeding.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Auther: bear
 * @Date: 2021/7/16 10:00
 * @Description: null
 */
public class DateUtil {
     public static String getCurrentDate(String aFormat) {
        String tDate = new SimpleDateFormat(aFormat).format(new java.util.Date(System.currentTimeMillis()));
        return tDate;
    }
      /**
     * 时间戳转换成日期格式字符串
     * @param miseconds 毫秒
     * @param format
     * @return
     */
    public static String timeStamp2Date(long miseconds,String format) {
        if(format == null || format.isEmpty()){
            format = "yyyy-MM-dd HH:mm:ss.fff";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(miseconds));
    }
    //将Java date转化为 数据库Date
    public static java.sql.Timestamp GetDatabaseTimestamp(java.util.Date d) {
        if (null == d)
            return null;
        return new java.sql.Timestamp(d.getTime());
    }

    //将数据库Date 转化为java date
    public static java.util.Date GetJavaDate(java.sql.Timestamp t) {
        if (null == t)
            return null;
        return new java.util.Date(t.getTime());
    }
    //当前时间加两个小时
    public static Date getDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 24小时制
        cal.add(Calendar.HOUR, 2);
        return cal.getTime();
    }
}
