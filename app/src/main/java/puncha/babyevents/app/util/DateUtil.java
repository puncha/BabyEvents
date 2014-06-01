package puncha.babyevents.app.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class DateUtil {
    public static Date utc2Local(Date utcDate) {
        Calendar cale = GregorianCalendar.getInstance();
        cale.setTime(utcDate);

        int zoneOffset = cale.get(java.util.Calendar.ZONE_OFFSET);
        int dstOffset = cale.get(java.util.Calendar.DST_OFFSET);
        cale.add(java.util.Calendar.MILLISECOND, (zoneOffset + dstOffset));
        return new Date(cale.getTimeInMillis());
    }

    public static Date local2Utc(Date localDate) {
        Calendar cale = GregorianCalendar.getInstance();
        cale.setTime(localDate);

        int zoneOffset = cale.get(java.util.Calendar.ZONE_OFFSET);
        int dstOffset = cale.get(java.util.Calendar.DST_OFFSET);
        cale.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        return new Date(cale.getTimeInMillis());
    }

    public static String ToSqliteAwareString(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static Date FromSqliteAwareString(String sqliteDate) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sqliteDate);
        } catch (ParseException e) {
            return new Date();
        }
    }
}