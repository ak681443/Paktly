package com.studypact.studypact.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import com.studypact.studypact.R;
import com.studypact.studypact.service.UsageTrackerService;
import com.studypact.studypact.util.Util;

public class SplashActivity extends AppCompatActivity {

    public static boolean isFirstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startService(new Intent(this, UsageTrackerService.class));

        Intent startActivity = null;
        if (!Util.getBooleanFromStore("is_setup")) {
            startActivity  = new Intent(this, ChooseAppsActivity.class);
        } else {
            startActivity = new Intent(this, HomeActivity.class);
        }
        startActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(startActivity);
    }
}
