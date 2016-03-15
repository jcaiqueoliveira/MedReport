package dev.tcc.caique.medreport.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
        //Todo https://developers.google.com/identity/sign-in/android/start-integrating
    }

    public void Sign(View view) {
    }

    public void enter(View view) {
        btnNewAccount.setText("Entrar");
        tvEmail.setVisibility(View.GONE);
    }
}
