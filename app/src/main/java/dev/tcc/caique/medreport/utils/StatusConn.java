package dev.tcc.caique.medreport.utils;

/**
 * Created by caique on 29/01/16.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
public class StatusConn {
    public static boolean isOnline(Context context) {
        boolean retorno = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            retorno = true;
        }
        return retorno;
    }

}