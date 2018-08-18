package ru.opennet.nix.opennetmvp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    private static final String GMT = "GMT";
    private static final String SERVER_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss Z";
    private static final String CLIENT_DATE_FORMAT = "dd.MM.yyyy HH:mm";

    public static String getNewsDate(String time) {
        Date date = stringToDate(time);
        SimpleDateFormat formatter = new SimpleDateFormat(CLIENT_DATE_FORMAT, Locale.getDefault());
        return formatter.format(date);
    }

    public static String getCommentDate(String time){
        Date date = stringToDate(time);
        /*TODO Это костыль, но что поделать... Я пока не знаю как ещё...
        FIXME Если воспользоваться методом getNewsDate, то получается, что время отстаёт на 2 часа*/
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, 2);
        SimpleDateFormat formatter = new SimpleDateFormat(CLIENT_DATE_FORMAT, Locale.getDefault());
        return formatter.format(cal.getTime());
    }

    private static Date stringToDate(String dateTime) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(SERVER_DATE_FORMAT, Locale.ENGLISH);
            format.setTimeZone(TimeZone.getTimeZone(GMT));
            return format.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
