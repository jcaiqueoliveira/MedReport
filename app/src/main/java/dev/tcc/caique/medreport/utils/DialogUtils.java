package dev.tcc.caique.medreport.utils;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import dev.tcc.caique.medreport.R;
import dev.tcc.caique.medreport.activities.LoginActivity;
import dev.tcc.caique.medreport.models.Singleton;

/**
 * Created by Avell B153 MAX on 20/02/2016.
 */
public class DialogUtils {

    public static void createDialogCloseApp(final FragmentActivity mContext, String text) {
        android.support.v7.app.AlertDialog alertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(text);
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Todo logout and navigate to login activity
                dialog.dismiss();
                final Firebase ref = new Firebase(Constants.BASE_URL);
                ref.unauth();
                mContext.startActivity(new Intent(mContext, LoginActivity.class));
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
    }

    public static void inviteFriend(final FragmentActivity mContext) {
        final Firebase firebase = new Firebase(Constants.BASE_URL + "users");
        final Firebase invites = new Firebase(Constants.BASE_URL);
        final Firebase friends = new Firebase(Constants.BASE_URL + "friends/" + firebase.getAuth().getUid());
        AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View v = mContext.getLayoutInflater().inflate(R.layout.search_friend, null);
        final EditText edit = (EditText) v.findViewById(R.id.email);
        builder.setTitle("Enviar convite");
        builder.setView(v);
        builder.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                Query queryRef = firebase.orderByChild("email").equalTo(edit.getText().toString());
                queryRef.addListenerForSingleValueEvent(new ValueEventListener() {//verifica se o email digitado existe
                    @Override
                    public void onDataChange(final DataSnapshot messageSnapshot) {
                        if (messageSnapshot.exists()) {// se o usuário existe verifica as outras condições
                            friends.orderByChild("email").equalTo(edit.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {//verifica se ja são amigos
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {//são amigos
                                        dialog.dismiss();
                                        Toast.makeText(mContext, "Vocês já estão conectados", Toast.LENGTH_SHORT).show();
                                    } else {//se não são amigos verifico se ja enviei um convite
                                        for (final DataSnapshot resultSearchFriend : messageSnapshot.getChildren()) {
                                            invites.child("invites").child(resultSearchFriend.getKey())
                                                    .orderByChild("email").equalTo((String) firebase.getAuth().getProviderData().get("email"))
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.exists()) {//convite ja enviado
                                                                dialog.dismiss();
                                                                Toast.makeText(mContext, "Um convite já foi enviado", Toast.LENGTH_SHORT).show();
                                                            } else {//envia novo convite
                                                                final Map<String, String> friendInvite = new HashMap<>();
                                                                friendInvite.put("email", (String) firebase.getAuth().getProviderData().get("email"));
                                                                friendInvite.put("name", Singleton.getInstance().getName());
                                                                Firebase reference = firebase.push();
                                                                friendInvite.put("stackId", reference.getKey());
                                                                invites.child("invites").child(resultSearchFriend.getKey()).child(reference.getKey()).setValue(friendInvite, new Firebase.CompletionListener() {
                                                                    @Override
                                                                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                                                        if (firebaseError != null) {
                                                                            dialog.dismiss();
                                                                            Toast.makeText(mContext, "Ocorreu um erro. Tente novamente", Toast.LENGTH_SHORT).show();
                                                                        } else {
                                                                            dialog.dismiss();
                                                                            Toast.makeText(mContext, "Convite enviado", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        }
                                                        @Override
                                                        public void onCancelled(FirebaseError firebaseError) {
                                                        }
                                                    });
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });

                        } else {
                            dialog.dismiss();
                            Toast.makeText(mContext, "Usuário não encontrado", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        Log.i("error", firebaseError.getMessage());
                    }
                });
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    public static void deleteAccompanimentDialog(final FragmentActivity mContext, final String userId, final String chatId) {
        android.support.v7.app.AlertDialog alertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Deseja apagar este acompanhamento?");
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final Firebase ref = new Firebase(Constants.BASE_URL);
                ref.child("friends").child(ref.getAuth().getUid()).child(userId).removeValue();
                ref.child("chat").child(chatId).removeValue();
                dialog.dismiss();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
    }
}
