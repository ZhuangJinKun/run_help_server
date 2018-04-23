package com.zjk.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
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
}
