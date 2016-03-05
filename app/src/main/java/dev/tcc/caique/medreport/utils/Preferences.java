package dev.tcc.caique.medreport.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Avell B153 MAX on 05/03/2016.
 */

public class Preferences {
    private static final String USER_EMAIL = "USER";

    public static String getUserEmail(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_EMAIL, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_EMAIL, "");
    }

    public static void setUserEmail(Context context,String email){
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_EMAIL, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(USER_EMAIL, email).apply();
    }
}
