package com.rf17.nexpenses.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rf17.nexpenses.NexpensesApplication;
import com.rf17.nexpenses.R;
import com.rf17.nexpenses.utils.AppPreferences;
import com.rf17.nexpenses.utils.UtilsApp;

public class SettingsActivity extends PreferenceActivity {
    // Load Settings
    private AppPreferences appPreferences;
    private Toolbar toolbar;

    // Settings variables
    //private AmbilWarnaPreference prefPrimaryColor;
    //private Preference prefNavigationColor;

    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        this.context = this;
        this.appPreferences = NexpensesApplication.getAppPreferences();

        UtilsApp.setAppColor(getWindow(), toolbar);

        toolbar.setTitle(getResources().getString(R.string.action_settings));

        // Configurações
        findPreference("prefPassword").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue instanceof Boolean) {
                    Boolean value = (Boolean) newValue;
                    appPreferences.setPasswordBooleanPref(value);
                    //Abre dialog
                }
                return true;
            }
        });

        // Temas
        //prefPrimaryColor = (AmbilWarnaPreference) findPreference("prefPrimaryColor");
        //AmbilWarnaPreference prefAccentColor = (AmbilWarnaPreference) findPreference("prefAccentColor");
        //prefNavigationColor = findPreference("prefNavigationColor");
        //Preference prefDefaultValues = findPreference("prefDefaultValues");

        // Backup

        // Sobre
        Preference prefVersion = findPreference("prefVersion");
        prefVersion.setTitle(getResources().getString(R.string.app_name) + " v" + UtilsApp.getAppVersionName(context) + " (" + UtilsApp.getAppVersionCode(context) + ")");
        prefVersion.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.fade_back);
                return true;
            }
        });

    }

    @Override
    public void setContentView(int layoutResID) {
        ViewGroup contentView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.activity_settings, new LinearLayout(this), false);
        toolbar = (Toolbar) contentView.findViewById(R.id.toolbar);////TODO Toolbar should load the default style in XML (white title and back arrow), but doesn't happen
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

    @Override
    public void onBackPressed() {
        context.startActivity(new Intent(context, MainActivity.class));
        overridePendingTransition(R.anim.fade_forward, R.anim.slide_out_right);
        finish();
    }

}
