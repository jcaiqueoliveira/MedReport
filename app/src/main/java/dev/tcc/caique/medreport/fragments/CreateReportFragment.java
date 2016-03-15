package dev.tcc.caique.medreport.fragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.firebase.client.Firebase;
import com.gun0912.tedpicker.ImagePickerActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.tcc.caique.medreport.R;
import dev.tcc.caique.medreport.activities.MainActivity;
import dev.tcc.caique.medreport.adapters.ImageAdapter;
import dev.tcc.caique.medreport.models.Report;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateReportFragment extends Fragment {
    @Bind(R.id.gridview)
    GridView gridView;
    @Bind(R.id.addImages)
    Button btnAdd;
    @Bind(R.id.title)
    EditText title;
    @Bind(R.id.description)
    EditText description;

    private static final int INTENT_REQUEST_GET_IMAGES = 13;

    private ArrayList<Bitmap> images;

    public CreateReportFragment() {
        // Required empty public constructor
    }

    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_create_report, container, false);
        ButterKnife.bind(this, v);
        ((MainActivity) getActivity()).fab.hide();
        setHasOptionsMenu(true);
        return v;
    }

    @OnClick(R.id.addImages)
    public void addImages() {
        //Prompt dialog to user select between camera and gallery
        openGalleryToSelectImages();
    }

    private void openGalleryToSelectImages() {
        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
        startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_REQUEST_GET_IMAGES && resultCode == Activity.RESULT_OK) {
            ArrayList<Uri> imageUris = data.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
            images = getBitmapsFromImageUris(imageUris);
            if (imageUris.size() > 0) {
                gridView.setAdapter(new ImageAdapter(getActivity(), images));
            }
        }
    }

    private ArrayList<Bitmap> getBitmapsFromImageUris(ArrayList<Uri> uris) {
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        for (Uri uri : uris) {
            try {
                InputStream image_stream = getActivity().getContentResolver().openInputStream(Uri.parse("file://" + uri));
                Bitmap bitmap = BitmapFactory.decodeStream(image_stream);
                bitmaps.add(bitmap);
                bitmaps.add(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmaps;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.save_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                try {
                    Firebase ref = new Firebase("https://medreportapp.firebaseio.com/");
                    // Map<String, Map<String, Report>> report = new HashMap<String,Firebase>();
                    Report r = new Report(title.getText().toString(), description.getText().toString());//, createListImagesCompress());
                    Map<String, String> post2 = new HashMap<String, String>();
                    post2.put("title", "alanisawesome");
                    post2.put("description", "The Turing Machine");
                    ref.child("users").child("52a1cba1-a170-49ca-891d-65ae3a38d84f").child("report").push().setValue(post2);
                    String uid = ref.getKey();
                    ArrayList<String> strings = createListImagesCompress();
                    for (String s : strings) {
                        Log.i("aqui", "uid" + uid);
                        Map<String, String> images = new HashMap<String, String>();
                        images.put("image", s);
                        ref.child("users").child("52a1cba1-a170-49ca-891d-65ae3a38d84f").child("report").
                                child(uid)
                                .child("image")
                                .push().setValue(images);
                    }

                    //report.put("report", ref.push());
                    // usersRef.setValue(users);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<String> createListImagesCompress() {
        ArrayList<String> listImages = new ArrayList<>();
        if (images.size() > 0) {
            for (Bitmap bmp : images) {
                ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, bYtE);
                bmp.recycle();
                byte[] byteArray = bYtE.toByteArray();
                String imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
                listImages.add(imageFile);
            }
        } else {
            Log.i("maior que ", "0");
        }
        return listImages;
    }
}
