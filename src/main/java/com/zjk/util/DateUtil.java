package com.zjk.util;

import com.zjk.common.DefSportsData;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by pandengzhe on 2018/4/7.
 */

public class DateUtil {

	public static String dateToString(Date date) {
		SimpleDateFormat format = new SimpleDateFormat(GsonUtil.DATE_PATTEN);
		return format.format(date);
	}

	public static String dateToFileNameString(Date date) {
		SimpleDateFormat format = new SimpleDateFormat(GsonUtil.DATE_FILE_PATTEN);
		return format.format(date);
	}

	public static Date stringToDate(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat(GsonUtil.DATE_PATTEN);
		ParsePosition pos = new ParsePosition(0);
		return formatter.parse(strDate, pos);
	}

	public static boolean isYesterday(Date date) {
		Date curDate = new Date();
		String standardDate = dateToString(curDate);
		standardDate = standardDate.split(" ")[0];
		standardDate += " 00:00:00";
		try {
			curDate = stringToDate(standardDate);
			return date.before(curDate);
		} catch (NullPointerException e) {
			return true;
		}
	}

	public static int getAge(Date birthDay) {
		Calendar cal = Calendar.getInstance();
		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH);
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(birthDay);

		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;
		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				if (dayOfMonthNow < dayOfMonthBirth)
					age--;
			} else {
				age--;
			}
		}
		return age;
	}

	public static Date getDateByDays(int day) {
		SimpleDateFormat formatter = new SimpleDateFormat(GsonUtil.DATE_PATTEN);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		if (dayWeek == 1) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		cal.setFirstDayOfWeek(Calendar.SUNDAY);
		int d = cal.get(Calendar.DAY_OF_WEEK);
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - d);
		cal.add(Calendar.DATE, day + 6);
		String stringDate = formatter.format(cal.getTime());
		stringDate = stringDate.split(" ")[0];
		stringDate = stringDate + " " + DefSportsData.EIGHTEEN_CLOCK;
		return stringToDate(stringDate);
	}

	/**
	 * 取得指定日期是星期几对应的index
	 *
	 * @param date Date
	 * @return index
	 */
	public static int getDayIndexOfTheWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int index = calendar.get(Calendar.DAY_OF_WEEK);
		if (index < 0) {
			index = 0;
		}
		return index;
	}

	/**
	 * 取得指定日期是星期几
	 *
	 * @param date Date
	 * @return String
	 */
	public static String getDayOfTheWeek(Date date) {
		String[] weeks = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
		return weeks[getDayIndexOfTheWeek(date)];
	}

	/**
	 * 取得日期是某年的第几周
	 *
	 * @param date Date
	 * @return int
	 */
	public static int getWeekOfYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 取得某个月有多少天
	 *
	 * @param year  int
	 * @param month int
	 * @return int
	 */
	public static int getDaysOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 取得两个日期之间的相差多少天
	 *
	 * @param date0 Date
	 * @param date1 Date
	 * @return long
	 */
	public static long getDaysBetween(Date date0, Date date1) {
		return (date0.getTime() - date1.getTime() + 1000000) / 86400000; // 86400000=3600*24*1000  用立即数，减少乘法计算的开销
	}

}
