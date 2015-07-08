package com.rf17.nexpenses.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

public class UtilsApp {
    // Load Settings
    private static AppPreferences appPreferences;

    public static File getAppFolder() {
        return new File(Environment.getExternalStorageDirectory() + "/MLManager");
    }

    public static Intent goToGooglePlay(String id) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + id));

        return intent;
    }

    public static Intent goToGooglePlus(String id) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://plus.google.com/" + id));

        return intent;
    }

    public static Intent goToSite(String site) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://" + site));

        return intent;
    }

    public static String getAppVersionName(Context context) {
        String res = "0.0.0.0";
        try {
            res = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    public static int getAppVersionCode(Context context) {
        int res = 0;
        try {
            res = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

}
