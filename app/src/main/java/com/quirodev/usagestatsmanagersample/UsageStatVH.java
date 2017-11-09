package com.quirodev.usagestatsmanagersample;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.Calendar;
import java.util.Map;

public class UsageStatVH extends RecyclerView.ViewHolder {

    private ImageView appIcon;
    private TextView appName;
    private TextView lastTimeUsed;
    private TextView lastTimeUsedDayTwo;
    private UsageStatsManager usageStatsManager;
    private PackageManager packageManager;
    private Context context;

    public UsageStatVH(View itemView) {
        super(itemView);

        appIcon = (ImageView) itemView.findViewById(R.id.icon);
        appName = (TextView) itemView.findViewById(R.id.title);
        lastTimeUsed = (TextView) itemView.findViewById(R.id.last_used);
        lastTimeUsedDayTwo = (TextView) itemView.findViewById(R.id.last_used_day_two);
    }

    private long getStartTime(int span) {
        Calendar calendar = Calendar.getInstance();
        //calendar.add(Calendar.YEAR, -1);
        calendar.add(Calendar.DATE, span);
        return calendar.getTimeInMillis();
    }

    public void bindTo(UsageStatsWrapper usageStatsWrapper) {

        appIcon.setImageDrawable(usageStatsWrapper.getAppIcon());
        appName.setText(usageStatsWrapper.getAppName());

        context = MainActivity.getContext();

        usageStatsManager = (UsageStatsManager) context.getSystemService("usagestats");
        packageManager = context.getPackageManager();

        Map<String, UsageStats> usageStatsDayTwo = usageStatsManager.queryAndAggregateUsageStats(getStartTime(-3), getStartTime(-2));

        Map<String, UsageStats> usageStatsDayThree = usageStatsManager.queryAndAggregateUsageStats(getStartTime(-4), getStartTime(-3));

        if (usageStatsWrapper.getUsageStats() == null){
            lastTimeUsed.setText(R.string.last_time_used_never);
        }else if (usageStatsWrapper.getUsageStats().getLastTimeUsed() == 0L){
            lastTimeUsed.setText(R.string.last_time_used_never);
        } else{
//            lastTimeUsed.setText(App.getApp().getString(R.string.last_time_used,
//                    DateUtils.format(usageStatsWrapper)));
            lastTimeUsed.setText(DateUtils.formatDate(usageStatsWrapper) + "   " + DateUtils.format(usageStatsWrapper));
            lastTimeUsedDayTwo.setText("text");
        }
    }
}
