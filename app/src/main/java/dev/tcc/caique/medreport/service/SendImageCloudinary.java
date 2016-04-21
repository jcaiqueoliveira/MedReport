package dev.tcc.caique.medreport.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dev.tcc.caique.medreport.models.Image;
import dev.tcc.caique.medreport.models.Singleton;
import dev.tcc.caique.medreport.utils.Constants;

/**
 * Created by Avell B153 MAX on 21/04/2016.
 */
public class SendImageCloudinary extends Service {
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


    private ArrayList<Image> uploadToCloudinary(ArrayList<InputStream> inputStream, String stackId) throws IOException {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", Constants.CLOUDINARY_NAME);
        config.put("api_key", Constants.CLOUDINARY_API_KEY);
        config.put("api_secret", Constants.CLOUDINARY_API_SECRET);
        Cloudinary cloudinary = new Cloudinary(config);
        ArrayList<Image> images = new ArrayList<>();
        for (int i = 0; i < inputStream.size(); i++) {
            Map uploadResult = cloudinary.uploader().upload(inputStream.get(i), ObjectUtils.emptyMap());
            images.add(new Image((String) uploadResult.get("secure_url"), (String) uploadResult.get("public_id")));
        }
        return images;
    }

    public class Upload extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Firebase uploadImg = new Firebase(Constants.BASE_URL);
                for (Image i : uploadToCloudinary(Singleton.getInstance().getInputStreams(), Singleton.getInstance().getTimeStampReport())) {
                    uploadImg.child("images").child(Singleton.getInstance().getTimeStampReport()).push().setValue(i, new Firebase.CompletionListener() {
                        @Override
                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                            //  getActivity().onBackPressed();
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(SendImageCloudinary.this, "Relatório criado com sucesso!", Toast.LENGTH_SHORT).show();
            onDestroy();
            super.onPostExecute(aVoid);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
