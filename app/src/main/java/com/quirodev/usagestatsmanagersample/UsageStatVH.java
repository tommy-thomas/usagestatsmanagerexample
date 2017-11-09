package com.quirodev.usagestatsmanagersample;

import android.app.usage.UsageStats;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class UsageStatVH extends RecyclerView.ViewHolder {

    private ImageView appIcon;
    private TextView appName;
    private TextView lastTimeUsed;
    private TextView lastTimeUsedDayTwo;
    private TextView lastTimeUsedDayThree;

    public UsageStatVH(View itemView) {
        super(itemView);

        appIcon = (ImageView) itemView.findViewById(R.id.icon);
        appName = (TextView) itemView.findViewById(R.id.title);
        lastTimeUsed = (TextView) itemView.findViewById(R.id.last_used);
        lastTimeUsedDayTwo = (TextView) itemView.findViewById(R.id.last_used_day_two);
        lastTimeUsedDayThree = (TextView) itemView.findViewById(R.id.last_used_day_three);
    }


    public void bindTo(UsageStatsWrapper usageStatsWrapper) {

        appIcon.setImageDrawable(usageStatsWrapper.getAppIcon());
        appName.setText(usageStatsWrapper.getAppName());

        if(usageStatsWrapper.getUsageStats() != null && !usageStatsWrapper.getUsageStats().isEmpty()  ){
            if (usageStatsWrapper.getUsageStats().get(0) == null){
                lastTimeUsed.setText(R.string.last_time_used_never);
            }else if (usageStatsWrapper.getUsageStats().get(0).getLastTimeUsed() == 0L){
                lastTimeUsed.setText(R.string.last_time_used_never);
            } else{
//            lastTimeUsed.setText(App.getApp().getString(R.string.last_time_used,
//                    DateUtils.format(usageStatsWrapper)));
                UsageStats stats = usageStatsWrapper.getUsageStats().get(0);
                lastTimeUsed.setText(DateUtils.formatDate(stats) + "   " + DateUtils.format(stats));
            }

            if ( usageStatsWrapper.getUsageStats().size() > 1 ) {
//            lastTimeUsed.setText(App.getApp().getString(R.string.last_time_used,
//                    DateUtils.format(usageStatsWrapper)));
                UsageStats stats = usageStatsWrapper.getUsageStats().get(1);
                lastTimeUsedDayTwo.setText(DateUtils.formatDate(stats) + "   " + DateUtils.format(stats));
            }


                if ( usageStatsWrapper.getUsageStats().size() > 2 ) {
//            lastTimeUsed.setText(App.getApp().getString(R.string.last_time_used,
//                    DateUtils.format(usageStatsWrapper)));
                UsageStats stats = usageStatsWrapper.getUsageStats().get(2);
                lastTimeUsedDayThree.setText(DateUtils.formatDate(stats) + "   " + DateUtils.format(stats));
            }


        }

    }
}
