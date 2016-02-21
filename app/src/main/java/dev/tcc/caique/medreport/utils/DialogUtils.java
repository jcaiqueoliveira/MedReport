package dev.tcc.caique.medreport.utils;


import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import dev.tcc.caique.medreport.R;

/**
 * Created by Avell B153 MAX on 20/02/2016.
 */
public class DialogUtils {

    public static void createDialogCloseApp(final FragmentActivity mContext, String text){
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
}
