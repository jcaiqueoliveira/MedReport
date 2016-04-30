package dev.tcc.caique.medreport.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.tcc.caique.medreport.R;
import dev.tcc.caique.medreport.activities.MainActivity;
import dev.tcc.caique.medreport.adapters.AdapterEditReport;
import dev.tcc.caique.medreport.adapters.ImageAdapter;
import dev.tcc.caique.medreport.models.Image;
import dev.tcc.caique.medreport.models.Report;
import dev.tcc.caique.medreport.models.Singleton;
import dev.tcc.caique.medreport.service.SendImageCloudinary;
import dev.tcc.caique.medreport.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateReportFragment extends Fragment {

    @Bind(R.id.gridview)
    GridView gridView;
    @Bind(R.id.title)
    EditText title;
    @Bind(R.id.description)
    EditText description;
    @Bind(R.id.inputDescription)
    TextInputLayout inputDescription;
    @Bind(R.id.inputTitle)
    TextInputLayout inputTitle;
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    private ArrayList<Bitmap> images;
    private Bundle bundle;
    private boolean isEditingMode = false;
    private Report report;
    private ArrayList<InputStream> inputStreams = new ArrayList<>();
    private Firebase uploadImg;
    private String timeStamp;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_WRITE_CAMERA = 2;
    private ArrayList<Image> urls = new ArrayList<>();
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
        bundle = getArguments();
        if (bundle != null) {
            report = (Report) bundle.getSerializable("report");
            if (report != null) {
                isEditingMode = true;
                title.setText(report.getTitle());
                description.setText(report.getDescription());
                Firebase firebase = new Firebase(Constants.BASE_URL + "images/" + report.getStackId());
                firebase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                     for(DataSnapshot ds: dataSnapshot.getChildren()){
                        urls.add(ds.getValue(Image.class));
                        // Log.i("valor",ds.getValue(Image.class).toString());
                     }
                     gridView.setAdapter(new AdapterEditReport(urls,getActivity()));
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        }
        uploadImg = new Firebase(Constants.BASE_URL);
        return v;
    }

    @OnClick(R.id.addImages)
    public void addImages() {
        //Prompt dialog to user select between camera and gallery
        dispatchTakePictureIntent();
    }

    private void openGalleryToSelectImages() {
        Config c = new Config();
        c.setSelectionLimit(4);
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
            for (Uri uri : imageUris) {
                try {
                    InputStream in = getActivity().getContentResolver().openInputStream(Uri.parse("file://" + uri));
                    inputStreams.add(in);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            Singleton.getInstance().setInputStreams(inputStreams);
        }
    }

    private ArrayList<Bitmap> getBitmapsFromImageUris(ArrayList<Uri> uris) {
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        for (Uri uri : uris) {
            try {
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inJustDecodeBounds = true;
                o.inSampleSize = 6;
                InputStream image_stream;
                final int REQUIRED_SIZE = 75;

                int scale = 1;
                while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                        o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                    scale *= 2;
                }
                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inSampleSize = scale;
                image_stream = getActivity().getContentResolver().openInputStream(Uri.parse("file://" + uri));

                Bitmap selectedBitmap = BitmapFactory.decodeStream(image_stream, null, o2);
                bitmaps.add(selectedBitmap);
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
                if (verifyFields()) {
                    final Firebase ref = new Firebase(Constants.BASE_URL);
                    //if (chosenFile != null) {
                    //  createUpload(chosenFile);
                    //  new UploadService(getActivity()).Execute(upload, new UiCallback());
                    if (isEditingMode) {
                        Report r = new Report();
                        r.setDescription(description.getText().toString());
                        r.setTitle(title.getText().toString());
                        r.setStackId(report.getStackId());
                        ref.child("reports").child(ref.getAuth().getUid()).child(report.getStackId()).setValue(r, new Firebase.CompletionListener() {
                            @Override
                            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                if (firebaseError != null) {
                                    showSnackBar("Erro ao atualizar relatório. Tente novamente.");
                                } else {
                                    showSnackBar("Editado com sucesso");
                                }
                            }
                        });
                    } else {
                        try {

                            timeStamp = ref.push().getKey();
                            Singleton.getInstance().setTimeStampReport(timeStamp);
                            Report report = new Report();
                            report.setDescription(description.getText().toString());
                            report.setTitle(title.getText().toString());
                            report.setStackId(timeStamp);
                            ref.child("reports").child(ref.getAuth().getUid()).child(timeStamp).setValue(report, new Firebase.CompletionListener() {
                                @Override
                                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                    if (firebaseError != null) {
                                        showSnackBar("Erro ao salvar relatório. Tente novamente.");
                                    } else {
                                        getActivity().startService(new Intent(getActivity(), SendImageCloudinary.class));
                                        getActivity().onBackPressed();
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean verifyFields() {
        boolean seguir = true;
        if (TextUtils.isEmpty(title.getText().toString())) {
            inputTitle.setError("Campo Obrigatório");
            seguir = false;
        }
        if (TextUtils.isEmpty(description.getText().toString())) {
            inputDescription.setError("Campo Obrigatório");
            seguir = false;
        }
        return seguir;
    }

    private void showSnackBar(String msg) {
        Snackbar snack = Snackbar.make(v.findViewById(R.id.parent), msg, Snackbar.LENGTH_LONG);
        View view = snack.getView();
        ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(ContextCompat.getColor(getActivity(), android.R.color.white));
        snack.show();
    }

    private void dispatchTakePictureIntent() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            openGalleryToSelectImages();
            return;
        } else {
            this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    REQUEST_WRITE_CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_WRITE_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                openGalleryToSelectImages();
            else {
                Snackbar.make(v, "Permissão não garantida", Snackbar.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
