package com.quirodev.usagestatsmanagersample;

import android.app.usage.UsageStats;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DateUtils {

    //DateFormat format = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
    //return format.format(usageStatsWrapper.getUsageStats().getTotalTimeInForeground());

    public static String format(UsageStats usageStats) {

        DateFormat format = new SimpleDateFormat("mm:ss:SSS");
        return format.format(usageStats.getTotalTimeInForeground());
    }

    public static String formatDate(UsageStats usageStats) {
        DateFormat format = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
        return format.format(usageStats.getLastTimeStamp());

    }
}

