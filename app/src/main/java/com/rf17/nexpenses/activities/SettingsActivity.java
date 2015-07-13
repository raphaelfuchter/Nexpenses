package com.rf17.nexpenses.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.rf17.nexpenses.NexpensesApplication;
import com.rf17.nexpenses.R;
import com.rf17.nexpenses.utils.AppPreferences;
import com.rf17.nexpenses.utils.UtilsApp;
import com.rf17.nexpenses.utils.UtilsUI;

import yuku.ambilwarna.widget.AmbilWarnaPreference;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    // Load Settings
    private AppPreferences appPreferences;
    private Toolbar toolbar;

    // Settings variables
    private AmbilWarnaPreference prefPrimaryColor;
    private Preference prefNavigationColor;

    private ListPreference prefSortMode;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        this.context = this;
        this.appPreferences = NexpensesApplication.getAppPreferences();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        // Configurações
        prefSortMode = (ListPreference) findPreference("prefSortMode");

        // Temas
        prefPrimaryColor = (AmbilWarnaPreference) findPreference("prefPrimaryColor");
        AmbilWarnaPreference prefAccentColor = (AmbilWarnaPreference) findPreference("prefAccentColor");
        prefNavigationColor = findPreference("prefNavigationColor");
        Preference prefDefaultValues = findPreference("prefDefaultValues");

        // Backup

        // Sobre
        Preference prefVersion = findPreference("prefVersion");

        setInitialConfiguration();

        String versionName = UtilsApp.getAppVersionName(context);
        int versionCode = UtilsApp.getAppVersionCode(context);

        prefVersion.setTitle(getResources().getString(R.string.app_name) + " v" + versionName + " (" + versionCode + ")");
        prefVersion.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.fade_back);
                return true;
            }
        });

        // prefSortMode
        setSortModeSummary();

        // prefDefaultValues
        prefDefaultValues.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                appPreferences.setPrimaryColorPref(getResources().getColor(R.color.primary));
                appPreferences.setAccentColorPref(getResources().getColor(R.color.fab));
                return true;
            }
        });

    }

    @Override
    public void setContentView(int layoutResID) {
        ViewGroup contentView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.activity_settings, new LinearLayout(this), false);
        toolbar = (Toolbar) contentView.findViewById(R.id.toolbar);
        //TODO Toolbar should load the default style in XML (white title and back arrow), but doesn't happen
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ViewGroup contentWrapper = (ViewGroup) contentView.findViewById(R.id.content_wrapper);
        LayoutInflater.from(this).inflate(layoutResID, contentWrapper, true);
        getWindow().setContentView(contentView);

    }

    private void setInitialConfiguration() {
        toolbar.setTitle(getResources().getString(R.string.action_settings));

        // Android 5.0+ devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(UtilsUI.darker(appPreferences.getPrimaryColorPref(), 0.8));
            toolbar.setBackgroundColor(appPreferences.getPrimaryColorPref());
            if (appPreferences.getNavigationColorPref()) {
                getWindow().setNavigationBarColor(appPreferences.getPrimaryColorPref());
            }
        }

        // Pre-Lollipop devices
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            prefPrimaryColor.setEnabled(false);
            prefNavigationColor.setEnabled(false);
            prefNavigationColor.setDefaultValue(false);
        }
    }

    private void setSortModeSummary() {
        Integer sortValue = new Integer(appPreferences.getSortMode())-1;
        prefSortMode.setSummary(getResources().getStringArray(R.array.sortEntries)[sortValue]);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);

        if (pref == prefSortMode) {
            setSortModeSummary();
        }
    }

    @Override
    public void onBackPressed() {
        context.startActivity(new Intent(context, MainActivity.class));
        overridePendingTransition(R.anim.fade_forward, R.anim.slide_out_right);
        finish();
    }

}
