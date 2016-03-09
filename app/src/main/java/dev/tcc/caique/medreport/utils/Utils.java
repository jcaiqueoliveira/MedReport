package dev.tcc.caique.medreport.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by Avell B153 MAX on 05/03/2016.
 */
public class Utils {
    public static Account[] getAccounts(Context context){
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(context).getAccountsByType("com.google");
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                String possibleEmail = account.name;
                String type = account.type;
                Log.i("Email", possibleEmail);
                Log.i("Type", type);
            }
        }
        return  accounts;
    }

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

    public static void openCamera(FragmentActivity context){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        context.startActivityForResult(i,Constants.CAMERA_INTENT);
    }
}
