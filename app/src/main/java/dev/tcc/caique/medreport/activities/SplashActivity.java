package dev.tcc.caique.medreport.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

import dev.tcc.caique.medreport.R;
import dev.tcc.caique.medreport.utils.Constants;
import dev.tcc.caique.medreport.utils.StatusConn;
import dev.tcc.caique.medreport.utils.Utils;

public class SplashActivity extends AppCompatActivity {

    private Firebase ref;

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
               findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
            }
        }, 300);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ref = new Firebase(Constants.BASE_URL + "users");
                ref.addAuthStateListener(new Firebase.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(AuthData authData) {
                        if (authData != null) {
                            Utils.getMyData(SplashActivity.this, findViewById(R.id.progressBar),null);
                        } else {
                            findViewById(R.id.progressBar).setVisibility(View.GONE);
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                });

            }
        }, 2000);
    }
}
