package dev.tcc.caique.medreport.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Handler;

import dev.tcc.caique.medreport.R;
import dev.tcc.caique.medreport.utils.StatusConn;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if(!StatusConn.isOnline(getApplicationContext())){
            Snackbar.make(findViewById(R.id.coordinatorLayout),"Sem conexão com a internet",Snackbar.LENGTH_LONG).show();
        }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }, 2000);
        }
}
