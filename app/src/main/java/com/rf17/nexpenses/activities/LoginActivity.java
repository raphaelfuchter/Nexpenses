package com.rf17.nexpenses.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
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

        final AppPreferences appPreferences = NexpensesApplication.getAppPreferences();

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
                        EditText senha = (EditText) findViewById(R.id.pincode);
                        if (appPreferences.getPassword().equals(senha.getText().toString())) {//Verifica se a senha foi digitada corretamente
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            overridePendingTransition(R.anim.fade_forward, R.anim.slide_out_right);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Senha incorreta!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            );
        }
    }

    @Override
    public void onBackPressed() {
        finish();
		System.exit(0);
    }

}
