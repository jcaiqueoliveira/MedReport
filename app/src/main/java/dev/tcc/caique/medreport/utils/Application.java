package dev.tcc.caique.medreport.utils;

import com.firebase.client.Firebase;

/**
 * Caique Oliveira on 27/03/2016.
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
    }
}