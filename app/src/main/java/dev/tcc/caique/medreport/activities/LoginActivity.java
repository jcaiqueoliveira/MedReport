package dev.tcc.caique.medreport.activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.tcc.caique.medreport.R;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.tv_email)
    TextView tvEmail;

    @Bind(R.id.tv_user)
    TextView tvUser;

    @Bind(R.id.tv_password)
    TextView tvPass;

    @Bind(R.id.newAccountButton)
    TextView btnNewAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Firebase.setAndroidContext(this);
        //Todo https://developers.google.com/identity/sign-in/android/start-integrating
    }

    public void Sign(final View view) {
        final Firebase ref = new Firebase("https://medreportapp.firebaseio.com/");
        //TODO: Validate Email
        if(btnNewAccount.getText().toString().equals("Entrar")){
            if(tvUser.getText().length()>0 && tvEmail.getText().length()>0 && tvPass.getText().length()>0)
                ref.authWithPassword(tvEmail.getText().toString(), tvPass.getText().toString(), new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        Snackbar.make(view,"Usuário ou senha incorretos",Snackbar.LENGTH_SHORT);
                    }
                });
        }else{
            if(tvUser.getText().length()>0 && tvEmail.getText().length()>0 && tvPass.getText().length()>0)
                ref.createUser(tvEmail.getText().toString(), tvPass.getText().toString(), new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> result) {
                        Log.v("Login: ", "Successfully created user account with uid: " + result.get("uid"));
                        String uid =  result.get("uid").toString();
                        Map<String, Object> user = new HashMap<String, Object>();
                        user.put(uid+"/name",tvEmail.getText().toString());
                        user.put(uid + "/provider", "password");
                        ref.child("users").updateChildren(user);
                        //Todo: save in sharedprefereces
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                    @Override
                    public void onError(FirebaseError firebaseError) {
                        // there was an error
                    }
                });
        }
    }

    public void enter(View view) {
        btnNewAccount.setText("Entrar");
        //tvEmail.setVisibility(View.GONE);
    }
}
