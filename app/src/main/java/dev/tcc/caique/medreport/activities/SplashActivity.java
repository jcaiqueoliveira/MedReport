package dev.tcc.caique.medreport.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import dev.tcc.caique.medreport.R;
import dev.tcc.caique.medreport.models.ProfileMedical;
import dev.tcc.caique.medreport.models.Singleton;
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
                ref = new Firebase(Constants.BASE_URL + "users");
                ref.addAuthStateListener(new Firebase.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(AuthData authData) {
                        if (authData != null) {
                            Utils.getMyData(SplashActivity.this);
                        } else {
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                });

            }
        }, 2000);
    }


}
