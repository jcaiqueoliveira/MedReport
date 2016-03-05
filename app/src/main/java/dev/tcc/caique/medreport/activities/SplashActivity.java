package dev.tcc.caique.medreport.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import dev.tcc.caique.medreport.R;
import dev.tcc.caique.medreport.utils.Preferences;
import dev.tcc.caique.medreport.utils.StatusConn;
import dev.tcc.caique.medreport.utils.Utils;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (!StatusConn.isOnline(getApplicationContext())) {
            Snackbar.make(findViewById(R.id.coordinatorLayout), "Sem conexão com a internet", Snackbar.LENGTH_LONG).show();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Preferences.getUserEmail(getApplicationContext()) != null) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else if (Utils.getAccounts(getApplicationContext()).length > 0) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                finish();
            }
        }, 2000);
    }

}
