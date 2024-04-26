package com.steiner.app.utils;


import android.content.Context;
import android.content.SharedPreferences;

public class SPDataSave {

    private  Context context;

    public SPDataSave() {
        this.context = MyApplication.context;
    }

    public boolean sharedPreferenceExist(String DATA_NAME, String key) {
        SharedPreferences prefs = context.getSharedPreferences(DATA_NAME, 0);
        if (!prefs.contains(key)) {
            return true;
        } else {
            return false;
        }
    }

    public  void setInt(String DATA_NAME, String key, int value) {
        SharedPreferences prefs = context.getSharedPreferences(DATA_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public  int getInt(String DATA_NAME, String key) {
        SharedPreferences prefs = context.getSharedPreferences(DATA_NAME, 0);
        return prefs.getInt(key, 0);
    }

    public  void setStr(String DATA_NAME, String key, String value) {
        SharedPreferences prefs = context.getSharedPreferences(DATA_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public  String getStr(String DATA_NAME, String key) {
        SharedPreferences prefs = context.getSharedPreferences(DATA_NAME, 0);
        return prefs.getString(key, null);
    }

    public  void setBool(String DATA_NAME, String key, boolean value) {
        SharedPreferences prefs = context.getSharedPreferences(DATA_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public  boolean getBool(String DATA_NAME, String key) {
        SharedPreferences prefs = context.getSharedPreferences(DATA_NAME, 0);
        return prefs.getBoolean(key, false);
    }

    public void Clear(String DATA_NAME){
        SharedPreferences prefs = context.getSharedPreferences(DATA_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }
}