package com.steiner.app.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    public static Context context = null;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}