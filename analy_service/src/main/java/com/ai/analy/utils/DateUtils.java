package com.ai.analy.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

public class DateUtils {

	private static final Logger log = Logger.getLogger(DateUtils.class);

	public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	
	public static final String YYYYMMDD = "yyyyMMdd";
	
	public static final String YYYY_MM_DD = "yyyy-MM-dd";

	/**
	 * 间隔天数
	 * 
	 * @param beginDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @return 相差天数
	 */
	public static int betweenDays(Date beginDate, Date endDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		try {
			beginDate = sdf.parse(sdf.format(beginDate));
			endDate = sdf.parse(sdf.format(endDate));
		} catch (ParseException e) {
			log.error("", e);
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(beginDate);
		long minTime = cal.getTimeInMillis();
		cal.setTime(endDate);
		long maxTime = cal.getTimeInMillis();
		long betweenDays = (maxTime - minTime) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(betweenDays));
	}

	/**
	 * 日期加N天
	 * 
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date addDayas(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
		Date newDate = cal.getTime();
		return newDate;
	}

	/**
	 * 获取n分钟后的时间
	 * 
	 * @param n可以是负数
	 *            ，负数的话表示n小时前
	 * @return
	 * @author liangyi
	 */
	public static Timestamp getNMinutes(int n) {
		Calendar calender = Calendar.getInstance();
		calender.add(Calendar.MINUTE, n);
		Timestamp nMinutesLater = new Timestamp(calender.getTimeInMillis());

		return nMinutesLater;
	}

	/**
	 * 将指定格式的日期字符串转成Timestamp
	 * 
	 * @param time
	 * @param pattern
	 * @return
	 * @author mayt
	 */
	public static Timestamp getTimestamp(String time, String pattern) throws Exception{
		DateFormat format = new SimpleDateFormat(pattern);
		format.setLenient(false);
		Timestamp ts = new Timestamp(format.parse(time).getTime());;
		return ts;
	}

	/**
	 * 获取系统当前时间
	 * 
	 * @return
	 * @author liangyi
	 */
	public static Timestamp getSysDate(){
		Timestamp time = new Timestamp(System.currentTimeMillis());
		return time;
	}
	
	/**
	 * 根据指定的格式输入时间字符串
	 * 
	 * @param pattern
	 * @return
	 * @author liangyi
	 */
	public static String getDateString(String pattern){
		Timestamp time = getSysDate();
		DateFormat dfmt = new SimpleDateFormat(pattern);
		java.util.Date date = time;
		return dfmt.format(date);
	}
	
	/**
	 * 根据指定的格式输入时间字符串
	 * @param time
	 * @param pattern
	 * @return
	 */
	public static String getDateString(Timestamp time,String pattern){
		DateFormat dfmt = new SimpleDateFormat(pattern);
		java.util.Date date = time;
		return dfmt.format(date);
	}
	
	/**
	 * 获取n天后的时间
	 * 
	 * @param n可以是负数，负数的话表示n天前
	 * @return
	 * @author liangyi
	 */
	public static Timestamp getNDays(Date date,int n) {
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
		calender.add(Calendar.DATE, n);
		Timestamp nMinutesLater = new Timestamp(calender.getTimeInMillis());

		return nMinutesLater;
	}
	
	/**
	 * 获取日期date的n天后的时间
	 * 
	 * @param n可以是负数，负数的话表示n天前
	 * @return
	 * @author liangyi
	 */
	public static Timestamp getNDays(int n) {
		Calendar calender = Calendar.getInstance();
		calender.add(Calendar.DATE, n);
		Timestamp nMinutesLater = new Timestamp(calender.getTimeInMillis());

		return nMinutesLater;
	}

	
	 /**
     * 获取从startDate 到 endDate之间的日期列表
     * 
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     * @author liangyi
     */
    public static List<String> getDates(Timestamp startDate, Timestamp endDate,String pattern) throws ParseException {
        List<String> dates = new LinkedList<String>();
        
        Calendar start = Calendar.getInstance();
        
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        
        if (pattern==null) {
        	pattern=YYYYMMDD;
		}
		DateFormat dfmt = new SimpleDateFormat(pattern);
        while (!start.after(end)) {
            dates.add(dfmt.format(start.getTime()));
            start.add(Calendar.DAY_OF_YEAR, 1);
        }

        return dates;

    }
    
}
