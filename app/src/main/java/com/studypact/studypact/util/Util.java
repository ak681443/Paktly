package com.studypact.studypact.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.studypact.studypact.appinstance.AppInstance;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Arvind on 11/12/2016.
 */

public class Util {
    private static final String SPREF_NAME = "pref.xml",
                                TAG = "UTIL";

    public static void setText(View parentView, @IdRes int resId, String text){
        if(parentView != null) {
            setText((TextView) parentView.findViewById(resId), text);
        }
    }

    public static void setText(TextView textView, String text){
        if(textView!=null){
            textView.setText(text);
        }
    }

    public static void putIntoStore(String key, JSONObject value){
            putIntoStore(key, value.toString());
    }


    public static void putIntoStore(String key, String value){
        SharedPreferences pref = AppInstance.getInstance().getSharedPreferences(SPREF_NAME, Context.MODE_PRIVATE);
        pref.edit().putString(key, value).apply();
    }

    public static JSONObject getJSONFromStore(String key){
        JSONObject json = null;
        try {
            json = new JSONObject(getFromStore(key, "{}"));
        }catch (Exception e){
            Log.e(TAG, "error in json", e);
        }
        return json;
    }

    public static String getFromStore(String key){
        return getFromStore(key, null);
    }

    public static String getFromStore(String key, String defaultString){
        SharedPreferences pref = AppInstance.getInstance().getSharedPreferences(SPREF_NAME, Context.MODE_PRIVATE);
        return pref.getString(key, defaultString);
    }

    public static boolean getBooleanFromStore(String key) {
        SharedPreferences pref = AppInstance.getInstance().getSharedPreferences(SPREF_NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(key, false);
    }


    public static void putBooleanFromStore(String key, boolean value) {
        SharedPreferences pref = AppInstance.getInstance().getSharedPreferences(SPREF_NAME, Context.MODE_PRIVATE);
        pref.edit().putBoolean(key, value).apply();
    }

    public static String getInitials(String name){
        String components[] = name.split(" ");
        return components.length == 1 ? ""+components[0].charAt(0) : components[0].charAt(0) + "" + components[components.length-1].charAt(0);
    }


    public static int getDateDifference(long start, long end){
        return ((int)end / ((24*60*60*1000))) -  ((int)start / ((24*60*60*1000)));
    }

    public static void showToast(String msg) {
        Toast.makeText(AppInstance.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void doPOST(String url, JSONObject body) {
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = null;

        new FormBody.Builder()
                .add("message", "Your message")
                .build();

    }

    public static String doGET(String url) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
            // Do something with the response.
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

