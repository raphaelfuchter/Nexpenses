package com.javiersantos.mlmanager.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.javiersantos.mlmanager.MLManagerApplication;
import com.javiersantos.mlmanager.R;
import com.javiersantos.mlmanager.utils.AppPreferences;
import com.javiersantos.mlmanager.utils.UtilsApp;
import com.javiersantos.mlmanager.utils.UtilsUI;

public class AboutActivity extends AppCompatActivity {
    // Load Settings
    AppPreferences appPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        this.appPreferences = MLManagerApplication.getAppPreferences();

        setInitialConfiguration();
        setScreenElements();

    }

    private void setInitialConfiguration() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.action_about);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(UtilsUI.darker(appPreferences.getPrimaryColorPref(), 0.8));
            toolbar.setBackgroundColor(appPreferences.getPrimaryColorPref());
            if (!appPreferences.getNavigationBlackPref()) {
                getWindow().setNavigationBarColor(appPreferences.getPrimaryColorPref());
            }
        }

    }

    private void setScreenElements() {
        TextView header = (TextView) findViewById(R.id.header);
        TextView appNameVersion = (TextView) findViewById(R.id.app_name);
        CardView about_developer = (CardView) findViewById(R.id.about_developer);
        CardView about_googleplay = (CardView) findViewById(R.id.about_googleplay);
        CardView about_email = (CardView) findViewById(R.id.about_email);

        header.setBackgroundColor(appPreferences.getPrimaryColorPref());

        appNameVersion.setText(getResources().getString(R.string.app_name) + " v" + UtilsApp.getAppVersionName(getApplicationContext()));

        about_developer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(UtilsApp.goToSite("www.raphaelfuchter.com"));
            }
        });

        about_googleplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(UtilsApp.goToGooglePlay("com.rf17.nexpenses"));
            }
        });

        about_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(UtilsApp.goToSite("rf17@oulook.com.br"));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_forward, R.anim.slide_out_right);
    }

}
