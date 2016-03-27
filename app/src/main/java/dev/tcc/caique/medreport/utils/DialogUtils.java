package dev.tcc.caique.medreport.utils;


import android.content.DialogInterface;
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
                dialog.dismiss();
                mContext.finish();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
    }

    public static void inviteFriend(final FragmentActivity mContext) {
        AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View v = mContext.getLayoutInflater().inflate(R.layout.search_friend, null);
        final EditText edit = (EditText) v.findViewById(R.id.email);
        builder.setTitle("Adicionar");
        builder.setView(v);
        builder.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                final Firebase firebase = new Firebase(Constants.BASE_URL + "users");
                final Firebase invites = new Firebase(Constants.BASE_URL);
                if (!edit.getText().toString().equalsIgnoreCase((String) firebase.getAuth().getProviderData().get("email"))) {
                    Query queryRef = firebase.orderByChild("email").equalTo(edit.getText().toString());
                    queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot messageSnapshot) {
                            if (messageSnapshot.exists()) {
                                final Map<String, String> friend = new HashMap<>();
                                for (final DataSnapshot ds : messageSnapshot.getChildren()) {
                                    if (!Singleton.getInstance().getFriends().contains((String) ds.child("email").getValue())) {//checando se ja nao sao amigos
                                        friend.put("email", (String) firebase.getAuth().getProviderData().get("email"));
                                        friend.put("name", Singleton.getInstance().getName());
                                        Query queryInvite = invites.child(ds.getKey()).orderByChild("email").equalTo((String) ds.child("email").getValue());
                                        queryInvite.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.getKey() == null) {
                                                    Log.i("retorno", dataSnapshot.toString());
                                                    Firebase reference = firebase.push();
                                                    friend.put("stackId", reference.getKey());
                                                    invites.child("invites").child(ds.getKey()).child(reference.getKey()).setValue(friend, new Firebase.CompletionListener() {
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
                                                } else {
                                                    dialog.dismiss();
                                                    Toast.makeText(mContext, "Convite já enviado", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(FirebaseError firebaseError) {

                                            }
                                        });
                                    } else {
                                        dialog.dismiss();
                                        Toast.makeText(mContext, "Vocês já são amigos", Toast.LENGTH_SHORT).show();
                                    }
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
                } else {
                    Toast.makeText(mContext, "Não é possível adicionar o próprio usuário", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }
}
