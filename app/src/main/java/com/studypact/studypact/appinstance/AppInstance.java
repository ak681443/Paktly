package com.studypact.studypact.appinstance;

import android.app.Application;

/**
 * Created by Arvind on 11/12/2016.
 */

public class AppInstance extends Application {

    public static AppInstance mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static AppInstance getInstance(){
        return mInstance;
    }
}
