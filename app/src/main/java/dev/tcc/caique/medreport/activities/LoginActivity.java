package dev.tcc.caique.medreport.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import dev.tcc.caique.medreport.R;
import dev.tcc.caique.medreport.utils.Constants;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.tv_email)
    AppCompatEditText tvEmail;

    @Bind(R.id.tv_user)
    AppCompatEditText tvUser;

    @Bind(R.id.tv_password)
    AppCompatEditText tvPass;

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
        final Firebase ref = new Firebase(Constants.BASE_URL);
        //TODO: Validate Email
        if (btnNewAccount.getText().toString().equals("Entrar")) {
            if (tvEmail.getText().length() > 0 && tvPass.getText().length() > 0) {
                ref.authWithPassword(tvEmail.getText().toString(), tvPass.getText().toString(), new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        Snackbar.make(view, "Usuário ou senha incorretos", Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            if (!TextUtils.isEmpty(tvUser.getText().toString()) && !TextUtils.isEmpty(tvEmail.getText().toString()) && !TextUtils.isEmpty(tvPass.getText().toString()))
                ref.createUser(tvEmail.getText().toString(), tvPass.getText().toString(), new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> result) {
                        String uid = result.get("uid").toString();
                        Map<String, Object> user = new HashMap<String, Object>();
                        user.put(uid + "/name", tvUser.getText().toString());
                        user.put(uid + "/email", tvEmail.getText().toString());
                        ref.child("users").updateChildren(user);
                        Snackbar.make(view, "Usuário criado com sucesso, faça login para acessar o aplicativo", Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        Snackbar.make(view, "Erro ao criar usuário", Snackbar.LENGTH_SHORT).show();
                    }
                });
        }
    }

    public void enter(View view) {
        if (tvUser.getVisibility() == View.VISIBLE) {
            btnNewAccount.setText("Entrar");
            tvUser.setVisibility(View.GONE);
            ((TextView) view.findViewById(R.id.tv_enter)).setText("Clique aqui para criar uma nova conta");
        } else {
            btnNewAccount.setText("Criar Conta");
            tvUser.setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.tv_enter)).setText("Clique  aqui para entrar");
        }
    }
}
