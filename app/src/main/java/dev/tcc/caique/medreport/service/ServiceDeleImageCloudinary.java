package dev.tcc.caique.medreport.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dev.tcc.caique.medreport.models.Image;
import dev.tcc.caique.medreport.models.Singleton;
import dev.tcc.caique.medreport.utils.Constants;

/**
 * Caique Oliveira on 21/04/2016.
 */
public class ServiceDeleImageCloudinary extends Service {
    @Override
    public void onCreate() {
        new Upload().execute();
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    private void deleteImagesCloudinary(ArrayList<Image> images) {
        Log.i("images", images.toString());
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", Constants.CLOUDINARY_NAME);
        config.put("api_key", Constants.CLOUDINARY_API_KEY);
        config.put("api_secret", Constants.CLOUDINARY_API_SECRET);
        Cloudinary cloudinary = new Cloudinary(config);
        for (Image i : images)
            try {
                Map resultDestroy = cloudinary.uploader().destroy(i.getPublicId(), ObjectUtils.asMap("invalidate", true));
                Log.e("resul", resultDestroy.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public class Upload extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            deleteImagesCloudinary(Singleton.getInstance().getImageToDeleteCloudinary());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            onDestroy();
            super.onPostExecute(aVoid);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
