package com.rf17.nexpenses.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.rf17.nexpenses.NexpensesApplication;
import com.rf17.nexpenses.R;
import com.rf17.nexpenses.utils.AppPreferences;
import com.rf17.nexpenses.utils.UtilsApp;
import com.rf17.nexpenses.utils.UtilsUI;

public class LoginActivity extends AppCompatActivity {

    private Boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppPreferences appPreferences = NexpensesApplication.getAppPreferences();

        if(!appPreferences.getPasswordBooleanPref()){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            overridePendingTransition(R.anim.fade_forward, R.anim.slide_out_right);
        }else{
            setContentView(R.layout.login);

            UtilsApp.setAppColor(getWindow(), null);

            ImageView background_login = (ImageView) findViewById(R.id.background_login);
            if (UtilsUI.getDayOrNight() == 1) {
                background_login.setImageResource(R.drawable.header_day_big);
            } else {
                background_login.setImageResource(R.drawable.header_night_big);
            }

            findViewById(R.id.fab_entrar).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //Verifica login

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                        overridePendingTransition(R.anim.fade_forward, R.anim.slide_out_right);
                    }
                }
            );
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.tap_exit, Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

}
