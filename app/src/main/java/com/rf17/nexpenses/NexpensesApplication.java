package com.rf17.nexpenses;

import android.app.Application;

import com.rf17.nexpenses.utils.AppPreferences;

public class NexpensesApplication extends Application {

    private static AppPreferences sAppPreferences;

    @Override
    public void onCreate() {
        sAppPreferences = new AppPreferences(this);
        super.onCreate();
    }

    public static AppPreferences getAppPreferences() {
        return sAppPreferences;
    }
}
