package dev.tcc.caique.medreport.utils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import dev.tcc.caique.medreport.activities.MainActivity;
import dev.tcc.caique.medreport.models.ProfileMedical;
import dev.tcc.caique.medreport.models.ProfilePacient;
import dev.tcc.caique.medreport.models.Singleton;

/**
 * Caique Oliveira on 05/03/2016.
 */
public class Utils {
    public static void setViewAndChildrenEnabled(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                setViewAndChildrenEnabled(child, enabled);
            }
        }
    }

    public static void getMyData(final AppCompatActivity mContext, final View view, final ProgressDialog progressDialog) {
        Firebase ref = new Firebase(Constants.BASE_URL + "users");
        final Firebase ref2 = new Firebase(Constants.BASE_URL + "users/" + ref.getAuth().getUid());
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
            }
        });
        Query queryRef = ref.orderByChild("email").equalTo((String) ref.getAuth().getProviderData().get("email"));
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot messageSnapshot) {
                if (messageSnapshot.exists()) {
                    for (DataSnapshot ds : messageSnapshot.getChildren()) {
                        Singleton.getInstance().setName(ds.child("name").getValue().toString());
                        Singleton.getInstance().setType(ds.child("type").getValue().toString());
                        if (Singleton.getInstance().getType().equals("1")) {
                            ProfileMedical pm = ds.child("profile").getValue(ProfileMedical.class);
                            Singleton.getInstance().setPm(pm);
                            if (pm != null && pm.getName() != null)
                                Singleton.getInstance().setName(pm.getName());
                        } else {
                            ProfilePacient pp = ds.child("profile").getValue(ProfilePacient.class);
                            Singleton.getInstance().setPp(pp);
                            if (pp != null && pp.getName() != null)
                                Singleton.getInstance().setName(pp.getName());
                        }
                    }
                    if (view != null)
                        view.setVisibility(View.GONE);
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    mContext.startActivity(new Intent(mContext, MainActivity.class));
                    mContext.finish();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }


    public static ViewGroup.LayoutParams getChatUserMessageLayoutParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1.0f;
        params.gravity = Gravity.RIGHT;
        return params;
    }

}
