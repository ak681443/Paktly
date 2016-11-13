package com.studypact.studypact.service;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.content.Context;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.studypact.studypact.activity.HomeActivity;
import com.studypact.studypact.activity.WontBudgeActivity;
import com.studypact.studypact.appinstance.AppInstance;
import com.studypact.studypact.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class UsageTrackerService extends Service {
    Context mInstance = null;
    private static final String TAG = "UsageTracker";
    private static ArrayList<String> blockedApps = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = getApplicationContext();
        ArrayList<String> list = new ArrayList<>();
        try {
            JSONArray blockedAppsArr = new JSONArray();
            blockedAppsArr = new JSONArray(Util.getFromStore("locked_apps"));
            for (int i = 0; i < blockedAppsArr.length(); i++) {
                blockedApps.add(blockedAppsArr.getString(i));
            }
        } catch (Exception e) {

        }
        new Thread(new Runnable() {
            @Override
            public void run() {

                int n = 0;
                while (true) {
                    if (isPactPending()) {
                        if (isLockedAppRunning()) {
                            //DO something !!
                            Intent intent = new Intent(mInstance, WontBudgeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                            mInstance.startActivity(intent);
                            Log.w(TAG, "OMG OMG OMG");
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {

                        }
                        if (n % 10 == 0) {
                            lazyEvictPacts();
                        }
                        n = (n + 1) % 100;
                    } else {
                        try {
                            Thread.sleep(15000);
                        } catch (Exception e) {

                        }
                    }
                }
            }
        }).start();
    }

    private boolean isPactPending() {
        return Util.getJSONFromStore("pacts").length() > 0;
    }

    public static final int DAY_MILLIS = 24 * 60 * 60 * 1000;

    private void lazyEvictPacts() {
        try {
            JSONObject object = Util.getJSONFromStore("pacts");
            if (!object.has("pacts")) return;
            JSONArray array = object.getJSONArray("pacts");
            JSONArray output = new JSONArray();
            for (int i = 0; i < array.length(); i++) {
                JSONObject pact = array.getJSONObject(i);
                if ((pact.getLong("start") + DAY_MILLIS) > System.currentTimeMillis()) {
                    output.put(pact);
                } else {
                    //TODO :: Notify server that a pact was succesfull
                }
            }
            object.put("pacts", output);
            Util.putIntoStore("pacts", object);
        } catch (Exception e) {
            Log.e(TAG, "exception in lazy remove", e);
        }
    }

    private boolean isLockedAppRunning() {
        ActivityManager mActivityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        String mPackageName = null;
        if (Build.VERSION.SDK_INT > 20) {
            mPackageName = mActivityManager.getRunningAppProcesses().get(0).processName;
        } else {
            mPackageName = mActivityManager.getRunningTasks(1).get(0).topActivity.getPackageName();
        }
        return blockedApps.contains(mPackageName) || blockedApps.contains(getForegroundApp());
    }

    public String getForegroundApp() {
        String currentApp = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usm = (UsageStatsManager) AppInstance.getInstance().getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
            if (appList != null && appList.size() > 0) {
                TreeMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : appList) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                }
            }
        } else {
            ActivityManager am = (ActivityManager) AppInstance.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
            currentApp = tasks.get(0).processName;
        }

        return currentApp;
    }


    public static void refreshArray() {
        try {
            JSONArray blockedAppsArr;
            blockedAppsArr = new JSONArray(Util.getFromStore("locked_apps"));
            for (int i = 0; i < blockedAppsArr.length(); i++) {
                blockedApps.add(blockedAppsArr.getString(i));
            }
        } catch (Exception e) {

        }
    }
}

