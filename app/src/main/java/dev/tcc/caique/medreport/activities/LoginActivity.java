package dev.tcc.caique.medreport.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
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
import dev.tcc.caique.medreport.utils.Utils;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.tv_email)
    AppCompatEditText tvEmail;

    @Bind(R.id.tv_user)
    AppCompatEditText tvUser;

    @Bind(R.id.tv_password)
    AppCompatEditText tvPass;

    @Bind(R.id.newAccountButton)
    TextView btnNewAccount;

    @Bind(R.id.radioGroupType)
    RadioGroup radioGroupType;
    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        radioGroupType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                type = checkedId;
            }
        });
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
                        ProgressDialog progressDialog = dialogProgress();
                        progressDialog.show();
                        Utils.getMyData(LoginActivity.this,null,progressDialog);
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        Snackbar.make(view, "Usuário ou senha incorretos", Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            if (!TextUtils.isEmpty(tvUser.getText().toString()) && !TextUtils.isEmpty(tvEmail.getText().toString()) && !TextUtils.isEmpty(tvPass.getText().toString()) && type > 0) {
                ref.createUser(tvEmail.getText().toString(), tvPass.getText().toString(), new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> result) {
                        Log.e("Criou","Criou");
                        String uid = result.get("uid").toString();
                        Map<String, Object> user = new HashMap<String, Object>();
                        user.put("name", tvUser.getText().toString());
                        user.put("email", tvEmail.getText().toString());
                        user.put("type", type);
                        ref.child("users").child(uid).setValue(user, new Firebase.CompletionListener() {
                            @Override
                            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                if(firebaseError==null){
                                    Snackbar.make(view, "Usuário criado com sucesso, faça login para acessar o aplicativo", Snackbar.LENGTH_SHORT).show();
                                }else{
                                    Log.e("Error",firebaseError.getMessage());
                                    Snackbar.make(view, "Error", Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        Snackbar.make(view, "Erro ao criar usuário", Snackbar.LENGTH_SHORT).show();
                    }
                });
            } else {
                Snackbar.make(view, "Preencha todos os campos", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    public void enter(View view) {
        if (tvUser.getVisibility() == View.VISIBLE) {
            btnNewAccount.setText("Entrar");
            tvUser.setVisibility(View.GONE);
            ((TextView) view.findViewById(R.id.tv_enter)).setText("Clique aqui para criar uma nova conta");
            radioGroupType.setVisibility(View.GONE);
        } else {
            btnNewAccount.setText("Criar Conta");
            tvUser.setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.tv_enter)).setText("Clique  aqui para entrar");
            radioGroupType.setVisibility(View.VISIBLE);
        }
    }

    private ProgressDialog dialogProgress() {
        ProgressDialog progressDialog = new ProgressDialog(this, R.style.dialog);
        progressDialog.setMessage(getString(R.string.wait));
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        return progressDialog;
    }
}
