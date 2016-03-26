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
import dev.tcc.caique.medreport.models.Singleton;
import dev.tcc.caique.medreport.utils.Constants;
import dev.tcc.caique.medreport.utils.StatusConn;

public class SplashActivity extends AppCompatActivity {

    private Firebase ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Firebase.setAndroidContext(this);
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
                            getMyData();
                        } else {
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                });

            }
        }, 2000);
    }

    private void getMyData() {
        Firebase ref2 = new Firebase(Constants.BASE_URL + "invites/" + this.ref.getAuth().getUid());
        Query query = ref2.orderByChild("email");
        Singleton.getInstance().getFriends().clear();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Singleton.getInstance().getFriends().add((String) ds.child("email").getValue());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.i("Cancelado", "cancelado");
            }
        });
        Query queryRef = this.ref.orderByChild("email").equalTo((String) ref.getAuth().getProviderData().get("email"));
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot messageSnapshot) {
                if (messageSnapshot.exists()) {
                    for (DataSnapshot ds : messageSnapshot.getChildren()) {
                        Singleton.getInstance().setName(ds.child("name").getValue().toString());

                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
                } else {
                    Log.i("vazio", "vazio");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.i("error", firebaseError.getMessage());
            }
        });
    }
}
