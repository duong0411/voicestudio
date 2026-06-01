package xiaozhi.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;


public class DateUtils {
    
    public final static String DATE_PATTERN = "yyyy-MM-dd";
    
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public final static String DATE_TIME_MILLIS_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";


    
    public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

    
    public static String format(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }

    
    public static Date parse(String date, String pattern) {
        try {
            return new SimpleDateFormat(pattern).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getDateTimeNow() {
        return getDateTimeNow(DATE_TIME_PATTERN);
    }

    public static String getDateTimeNow(String pattern) {
        return format(new Date(), pattern);
    }

    public static String millsToSecond(long mills) {
        return String.format("%.3f", mills / 1000.0);
    }

    
    public static String getShortTime(Date date) {
        if (date == null) {
            return null;
        }

        LocalDateTime localDateTime = date.toInstant()

                .atZone(ZoneId.systemDefault())

                .toLocalDateTime();

        LocalDateTime now = LocalDateTime.now();

        long secondsBetween = ChronoUnit.SECONDS.between(localDateTime, now);

        if (secondsBetween <= 10) {
            return "";
        } else if (secondsBetween < 60) {
            return secondsBetween + "";
        } else if (secondsBetween < 60 * 60) {
            return secondsBetween / 60 + "";
        } else if (secondsBetween < 86400) {
            return secondsBetween / 3600 + "";
        } else if (secondsBetween < 604800) {
            return secondsBetween / 86400 + "";
        } else {

            return format(date,DATE_TIME_PATTERN);
        }
    }
}
