package dev.tcc.caique.medreport.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;

import java.util.regex.Pattern;

import dev.tcc.caique.medreport.R;
import dev.tcc.caique.medreport.models.Singleton;
import dev.tcc.caique.medreport.utils.StatusConn;

public class SplashActivity extends AppCompatActivity {
   private String[] account ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if(!StatusConn.isOnline(getApplicationContext())){
            Snackbar.make(findViewById(R.id.coordinatorLayout),"Sem conexão com a internet",Snackbar.LENGTH_LONG).show();
        }
        getAccounts();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(account.length > 0){
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        Singleton.getInstance().setAccount(account);
                    }else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                    finish();
                }
            }, 2000);
        }
    private void getAccounts(){
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account [] accounts = AccountManager.get(this).getAccountsByType("com.google");
        this.account = new String[accounts.length];
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                String possibleEmail = account.name;
                String type = account.type;
                Log.i("Email", possibleEmail);
                Log.i("Type", type);
            }
        }
    }
}
