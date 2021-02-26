package com.example.Stopi.profile.login;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.Stopi.tools.App;

public class SharedPrefs {

    private static String TAG = "SharedPrefs";
    private String FIRST_LOGIN = "FIRST_LOGIN";
    private String THEME = "THEME";

    private static SharedPrefs instance;
    private SharedPreferences prefs;

    //====================================================

    public static void initPrefs(){
        if(instance == null) {
            instance = new SharedPrefs();
            instance.prefs =  App.getAppContext().getSharedPreferences(TAG,Context.MODE_PRIVATE);
        }
    }

    /**
     * gets the singleton
     */
    public static SharedPrefs get() { return instance; }

    //====================================================

    public Boolean isFirstLogin(){ return prefs.getBoolean(FIRST_LOGIN, true); }

    public void saveFirstLogin(){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(FIRST_LOGIN,false);
        editor.apply();
    }

    public void deleteFirstLogin() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(FIRST_LOGIN);
        editor.commit();
    }

    //====================================================

    public int getTheme(){ return prefs.getInt(THEME, 0); }

    public void saveTheme(int theme) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(THEME,theme);
        editor.apply();
    }

    public void deleteSelectedTheme() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(THEME);
        editor.commit();
    }
}