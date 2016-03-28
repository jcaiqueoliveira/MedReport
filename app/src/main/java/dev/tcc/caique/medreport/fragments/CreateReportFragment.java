package dev.tcc.caique.medreport.fragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
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
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.tcc.caique.medreport.R;
import dev.tcc.caique.medreport.activities.MainActivity;
import dev.tcc.caique.medreport.adapters.ImageAdapter;
import dev.tcc.caique.medreport.models.Image;
import dev.tcc.caique.medreport.models.Report;
import dev.tcc.caique.medreport.utils.Constants;

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
    @Bind(R.id.inputDescription)
    TextInputLayout inputDescription;
    @Bind(R.id.inputTitle)
    TextInputLayout inputTitle;
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
        }
    }

    private ArrayList<Bitmap> getBitmapsFromImageUris(ArrayList<Uri> uris) {
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        for (Uri uri : uris) {
            try {
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inJustDecodeBounds = true;
                o.inSampleSize = 6;

                InputStream image_stream = getActivity().getContentResolver().openInputStream(Uri.parse("file://" + uri));
                Bitmap bitmap = BitmapFactory.decodeStream(image_stream, null, o);
                image_stream.close();
                final int REQUIRED_SIZE = 75;

                // Find the correct scale value. It should be the power of 2.
                int scale = 1;
                while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                        o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                    scale *= 2;
                }
                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inSampleSize = scale;
                image_stream = getActivity().getContentResolver().openInputStream(Uri.parse("file://" + uri));

                Bitmap selectedBitmap = BitmapFactory.decodeStream(image_stream, null, o2);
                image_stream.close();
                bitmaps.add(selectedBitmap);
                //bitmaps.add(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri));
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
                    try {
                        final Firebase ref = new Firebase(Constants.BASE_URL);
                        final String timeStamp = ref.push().getKey();
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
                                    //Todo send image
                                    ArrayList<String> images = createListImagesCompress();
                                    for (String s : images) {
                                        Image i = new Image();
                                        i.setImage(s);
                                        ref.child("images").child(timeStamp).push().setValue(i, new Firebase.CompletionListener() {
                                            @Override
                                            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                                if (firebaseError != null) {
                                                    Log.i("Error", firebaseError.getMessage());
                                                }
                                            }
                                        });
                                    }
                                    showSnackBar("Relatório criado com sucesso");
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, bYtE);
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
}
