package com.netkoin.app.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by siddharth on 1/11/2017.
 */
public class DateTimeUtils {

    public static String getComparedDateText(String utcTime) {
        try {
            if (utcTime == null)
                return "";

            //removing code after "+"
            if (utcTime.contains("+")) {
                String[] onlyUTCTime = utcTime.split("\\+");
                utcTime = onlyUTCTime[0];
            }

            DateFormat iso8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = iso8601.parse(utcTime);

            return getDifference(date, new Date(), false);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";

    }

    public static String remainingTime(String utcTime) {
        try {
            if (utcTime == null)
                return "";
            //removing code after "+"
            if (utcTime.contains("+")) {
                String[] onlyUTCTime = utcTime.split("\\+");
                utcTime = onlyUTCTime[0];
            }

            DateFormat iso8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = iso8601.parse(utcTime);
            return getDifference(new Date(), date, true);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDifference(Date startDate, Date endDate, boolean lessThanAmin) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays,
                elapsedHours, elapsedMinutes, elapsedSeconds);

        //check if day older
        if (elapsedDays == 1) {
            return elapsedDays + " day ago";
        } else if (elapsedDays > 1 && elapsedDays < 6) {
            return elapsedDays + " days ago";
        } else if (elapsedDays > 5) {
            if (lessThanAmin) {
                return convertMilliSecondsToFormattedDate(endDate.getTime());
            } else {
                return convertMilliSecondsToFormattedDate(startDate.getTime());
            }
        }

        //check if hour older
        if (elapsedHours == 1) {
            return elapsedHours + " hour " + elapsedMinutes + " min ago";
        } else if (elapsedHours > 1) {
            return elapsedHours + " hours " + elapsedMinutes + " min ago";
        }

        //check mins older
        if (elapsedMinutes > 0) {
            return elapsedMinutes + " min ago";
        } else {
            if (lessThanAmin) {
                //check seconds older
                return "less than one min";
            } else {
                //check seconds older
                return "just now";
            }
        }

    }

    public static String convertMilliSecondsToFormattedDate(long milliSeconds) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return simpleDateFormat.format(calendar.getTime());
    }
}
