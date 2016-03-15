package dev.tcc.caique.medreport.fragments;


import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import com.gun0912.tedpicker.ImagePickerActivity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.tcc.caique.medreport.R;
import dev.tcc.caique.medreport.activities.MainActivity;
import dev.tcc.caique.medreport.adapters.ImageAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateReportFragment extends Fragment {
    @Bind(R.id.gridview)
    GridView gridView;
    @Bind(R.id.addImages)
    Button btnAdd;

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
        return v;
    }

    @OnClick(R.id.addImages)
    public void addImages(){
        //Prompt dialog to user select between camera and gallery
        openGalleryToSelectImages();
    }

    private void openGalleryToSelectImages(){
        Intent intent  = new Intent(getActivity(), ImagePickerActivity.class);
        startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_REQUEST_GET_IMAGES && resultCode == Activity.RESULT_OK ) {
            ArrayList<Uri>  imageUris = data.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
            ArrayList<Bitmap> images = getBitmapsFromImageUris(imageUris);
            if (imageUris.size()>0){
                gridView.setAdapter(new ImageAdapter(getActivity(),images));
            }
        }
    }

    private ArrayList<Bitmap> getBitmapsFromImageUris(ArrayList<Uri> uris){
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        for(Uri uri : uris){
            try {
                InputStream image_stream = getActivity().getContentResolver().openInputStream(Uri.parse("file://"+uri));
                Bitmap bitmap= BitmapFactory.decodeStream(image_stream );
                bitmaps.add(bitmap);
                bitmaps.add(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uri));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmaps;
    }
}
