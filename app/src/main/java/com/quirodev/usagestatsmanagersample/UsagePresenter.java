package com.quirodev.usagestatsmanagersample;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static android.app.AppOpsManager.MODE_ALLOWED;
import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;
import static android.os.Process.myUid;

public class UsagePresenter implements UsageContract.Presenter {

    private static final int flags = PackageManager.GET_META_DATA |
            PackageManager.GET_SHARED_LIBRARY_FILES |
                PackageManager.GET_UNINSTALLED_PACKAGES;

    private UsageStatsManager usageStatsManager;
    private PackageManager packageManager;
    private UsageContract.View view;
    private final Context context;

    public UsagePresenter(Context context, UsageContract.View view) {
        usageStatsManager = (UsageStatsManager) context.getSystemService("usagestats");
        packageManager = context.getPackageManager();
        this.view = view;
        this.context = context;
    }

    private long getStartTime(int span) {
        Calendar calendar = Calendar.getInstance();
        //calendar.add(Calendar.YEAR, -1);
        calendar.add(Calendar.DATE, span);
        return calendar.getTimeInMillis();
    }

    @Override
    public void retrieveUsageStats() {
        if (!checkForPermission(context)) {
            view.onUserHasNoPermission();
            return;
        }

        List<String> installedApps = getInstalledAppList();

        Map<String, UsageStats> usageStats = usageStatsManager.queryAndAggregateUsageStats(getStartTime(-1), getStartTime(0));
        Map<String, UsageStats> usageStatsDayTwo = usageStatsManager.queryAndAggregateUsageStats(getStartTime(-2), getStartTime(-1));
        Map<String, UsageStats> usageStatsDayThree = usageStatsManager.queryAndAggregateUsageStats(getStartTime(-3), getStartTime(-2));

        List<UsageStats> stats = new ArrayList<>();
        stats.addAll(usageStats.values());

        List<UsageStats> statsDayTwo = new ArrayList<>();
        statsDayTwo.addAll(usageStatsDayTwo.values());

        List<UsageStats> statsDayThree = new ArrayList<>();
        statsDayThree.addAll(usageStatsDayThree.values());

        List<UsageStatsWrapper> finalList = buildUsageStatsWrapper(installedApps, stats , statsDayTwo , statsDayThree);
        view.onUsageStatsRetrieved(finalList);
    }

    private boolean checkForPermission(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS, myUid(), context.getPackageName());
        return mode == MODE_ALLOWED;
    }

    private List<String> getInstalledAppList(){
        List<ApplicationInfo> infos = packageManager.getInstalledApplications(flags);
        List<String> installedApps = new ArrayList<>();
        for (ApplicationInfo info : infos){
            installedApps.add(info.packageName);
        }
        return installedApps;
    }

    private List<UsageStatsWrapper> buildUsageStatsWrapper(List<String> packageNames, List<UsageStats> usageStatses , List<UsageStats> usageStatsDayTwo , List<UsageStats> usageStatsDayThree) {
        List<UsageStatsWrapper> list = new ArrayList<>();
        for (String name : packageNames) {
            boolean added = false;
            ArrayList<UsageStats> statsList = new ArrayList<>();
            for (UsageStats stat : usageStatses) {
                if (name.equals(stat.getPackageName())) {
                   // added = true;
                   // list.add(fromUsageStat(stat));
                    statsList.add(stat);
                }
            }
            for (UsageStats stat : usageStatsDayTwo) {
                if (name.equals(stat.getPackageName())) {
                    // added = true;
                    // list.add(fromUsageStat(stat));
                    statsList.add(stat);
                }
            }
            for (UsageStats stat : usageStatsDayThree) {
                if (name.equals(stat.getPackageName())) {
                    // added = true;
                    // list.add(fromUsageStat(stat));
                    statsList.add(stat);
                }
            }
            if (!statsList.isEmpty()) {
                list.add(fromUsageStat(statsList));
            } else {
                list.add(fromUsageStat(name));
            }
            Log.v("ListCount" , String.valueOf(statsList.size()));
        }
        Collections.sort(list);
        return list;
    }

    private UsageStatsWrapper fromUsageStat(String packageName) throws IllegalArgumentException {
        try {
            ApplicationInfo ai = packageManager.getApplicationInfo(packageName, 0);
            return new UsageStatsWrapper(null, packageManager.getApplicationIcon(ai), packageManager.getApplicationLabel(ai).toString());

        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private UsageStatsWrapper fromUsageStat(ArrayList<UsageStats> usageStats) throws IllegalArgumentException {
        try {
            ApplicationInfo ai = packageManager.getApplicationInfo(usageStats.get(0).getPackageName(), 0);
            return new UsageStatsWrapper(usageStats, packageManager.getApplicationIcon(ai), packageManager.getApplicationLabel(ai).toString());

        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
