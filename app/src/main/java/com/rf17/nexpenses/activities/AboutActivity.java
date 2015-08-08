package com.rf17.nexpenses.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rf17.nexpenses.NexpensesApplication;
import com.rf17.nexpenses.R;
import com.rf17.nexpenses.utils.AppPreferences;
import com.rf17.nexpenses.utils.UtilsApp;

public class AboutActivity extends AppCompatActivity {

    AppPreferences appPreferences;

    @Override
     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        this.appPreferences = NexpensesApplication.getAppPreferences();

        setInitialConfiguration();
        setScreenElements();
    }

    private void setInitialConfiguration() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null ) {
            getSupportActionBar().setTitle(R.string.action_about);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        UtilsApp.setAppColor(getWindow(), toolbar);
    }

    private void setScreenElements() {
        RelativeLayout header = (RelativeLayout) findViewById(R.id.header_about);
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
                Intent i = new Intent(Intent.ACTION_SENDTO);
                i.setData(Uri.parse("mailto:" + "rf17@outlook.com.br"));
                startActivity(Intent.createChooser(i, "Enviar email"));
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(AboutActivity.this, MainActivity.class);
        startActivity(myIntent);
        finish();
    }

}
