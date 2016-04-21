package dev.tcc.caique.medreport.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import dev.tcc.caique.medreport.R;
import dev.tcc.caique.medreport.activities.MainActivity;
import dev.tcc.caique.medreport.models.ProfilePacient;
import dev.tcc.caique.medreport.models.Singleton;
import dev.tcc.caique.medreport.utils.Constants;
import dev.tcc.caique.medreport.utils.Utils;

public class ProfilePacientFragment extends Fragment {
    private boolean isEditing = false;
    //http://code.tutsplus.com/tutorials/capture-and-crop-an-image-with-the-device-camera--mobile-11458
    //https://android-arsenal.com/details/1/205
    //https://github.com/lvillani/android-cropimage
    private View v;
    @Bind(R.id.imgProfile)
    CircleImageView imgProfile;
    @Bind(R.id.edtName)
    AppCompatEditText edtName;
    @Bind(R.id.edtAge)
    AppCompatEditText edtAge;
    @Bind(R.id.edtGender)
    AppCompatEditText edtGender;
    @Bind(R.id.edtHeight)
    AppCompatEditText edtHeight;
    @Bind(R.id.edtWeight)
    AppCompatEditText edtWeight;
    @Bind(R.id.edtProblem1)
    AppCompatEditText edtProblem1;
    @Bind(R.id.edtProblem2)
    AppCompatEditText edtProblem2;
    @Bind(R.id.edtProblem3)
    AppCompatEditText edtProblem3;
    @Bind(R.id.inputName)
    TextInputLayout inputName;
    static final int REQUEST_WRITE_CAMERA = 2;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Uri outputFileUri;
    private String file, dir;
    private ProgressDialog progress;

    public ProfilePacientFragment() {
        // Required empty public constructor
    }

    //Todo http://developer.android.com/guide/topics/ui/menus.html#ChangingTheMenu
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Todo criar condição para selecionar perfil medico ou paciente
        v = inflater.inflate(R.layout.fragment_profile_pacient, container, false);
        ButterKnife.bind(this, v);
        setHasOptionsMenu(true);
        Utils.setViewAndChildrenEnabled(v, false);
        ((MainActivity) getActivity()).fab.hide();
        edtName.setText(Singleton.getInstance().getName());
        if (Singleton.getInstance().getPp() != null) {
            ProfilePacient pp = Singleton.getInstance().getPp();
            edtName.setText(pp.getName());
            edtAge.setText(pp.getAge());
            edtGender.setText(pp.getGender());
            edtHeight.setText(pp.getStature());
            edtWeight.setText(pp.getWeight());
            edtProblem1.setText(pp.getHealthProblem());
            edtProblem2.setText(pp.getAllergy());
            edtProblem3.setText(pp.getDrugAllergy());
        }
        dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/MedReport/";
        if (Singleton.getInstance().getPp().getProfileUrl() != null) {
            Glide.with(getActivity()).load(Singleton.getInstance().getPp().getProfileUrl()).into(imgProfile);
        }
        return v;
    }

    @Override
    public void onResume() {
        ((MainActivity) getActivity()).navigationView.setCheckedItem(Constants.PROFILE);
        super.onResume();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.editProfile).setVisible(!isEditing);
        menu.findItem(R.id.saveProfile).setVisible(isEditing);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editProfile:
                isEditing = true;
                Utils.setViewAndChildrenEnabled(v, true);
                getActivity().invalidateOptionsMenu();
                break;
            case R.id.saveProfile:
                isEditing = false;
                if (TextUtils.isEmpty(edtName.getText().toString())) {
                    inputName.setError("Campo Obrigatório");
                } else {
                    new UpdateProfile().execute();
                    Utils.setViewAndChildrenEnabled(v, false);
                    getActivity().invalidateOptionsMenu();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.imgProfile})
    public void OnClick() {
        dispatchTakePictureIntent();
    }

    private void showSnackBar(String msg) {
        Snackbar snack = Snackbar.make(v.findViewById(R.id.parent), msg, Snackbar.LENGTH_LONG);
        View view = snack.getView();
        ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(ContextCompat.getColor(getActivity(), android.R.color.white));
        snack.show();
    }

    private ProfilePacient getFieldsProfile() {
        ProfilePacient profilePacient = new ProfilePacient();
        profilePacient.setName(edtName.getText().toString());
        profilePacient.setAge(edtAge.getText().toString());
        profilePacient.setGender(edtGender.getText().toString());
        profilePacient.setStature(edtHeight.getText().toString());
        profilePacient.setWeight(edtWeight.getText().toString());
        profilePacient.setHealthProblem(edtProblem1.getText().toString());
        profilePacient.setAllergy(edtProblem2.getText().toString());
        profilePacient.setDrugAllergy(edtProblem3.getText().toString());
        return profilePacient;
    }

    private void dispatchTakePictureIntent() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            startIntentCamera();
        } else {
            Log.i("a", "b");
            this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    REQUEST_WRITE_CAMERA);
        }
    }

    private void startIntentCamera() {
        outputFileUri = Uri.fromFile(createFile());
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
    }

    private File createFile() {
        createFolderIfNotExist();
        file = dir + "profilePicture.jpg";
        File newfile = new File(file);
        try {
            newfile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newfile;
    }

    private void createFolderIfNotExist() {
        File f = new File(dir);
        if (!f.exists()) {
            f.mkdir();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_WRITE_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                startIntentCamera();
            else {
                showSnackBar("Permissão não garantida");
                Log.i("0", "" + grantResults[0]);
                Log.i("1", "" + grantResults[1]);
            }
        } else {
            Log.i("request code", "" + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            Glide.with(getActivity()).load(outputFileUri).into(imgProfile);
            Log.i("uri", outputFileUri.toString());
            //PreferencesHelper.saveMyMotocycle(getActivity(), motoCycle, position);
        }
    }

    public class UpdateProfile extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            progress = dialogProgress();
            progress.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ProfilePacient profilePacient = getFieldsProfile();
            if (outputFileUri != null) {
                Map<String, String> config = new HashMap<>();
                config.put("cloud_name", Constants.CLOUDINARY_NAME);
                config.put("api_key", Constants.CLOUDINARY_API_KEY);
                config.put("api_secret", Constants.CLOUDINARY_API_SECRET);
                Cloudinary cloudinary = new Cloudinary(config);
                try {
                    Map result = cloudinary.uploader().upload(
                            getActivity().getContentResolver().openInputStream(Uri.parse(outputFileUri.toString())),
                            ObjectUtils.emptyMap());
                    Log.i("result", result.toString());
                    profilePacient.setPublicId((String) result.get("public_id"));
                    profilePacient.setProfileUrl((String) result.get("secure_url"));
                    updateUserInFirebase(profilePacient);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                updateUserInFirebase(profilePacient);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private void updateUserInFirebase(final ProfilePacient profilePacient) {
        Firebase f = new Firebase(Constants.BASE_URL);
        f.child("users").child(f.getAuth().getUid()).child("profile").setValue(profilePacient, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError == null) {
                    progress.dismiss();
                    showSnackBar("Perfil Atualizado");
                    Singleton.getInstance().setPp(profilePacient);
                } else {
                    progress.dismiss();
                    showSnackBar("Erro ao atualizar perfil. Tente novamente");

                }
            }
        });
    }

    private ProgressDialog dialogProgress() {
        ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.dialog);
        progressDialog.setMessage(getString(R.string.wait));
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        return progressDialog;
    }
}
