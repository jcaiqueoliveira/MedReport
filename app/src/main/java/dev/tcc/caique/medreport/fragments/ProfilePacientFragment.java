package dev.tcc.caique.medreport.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
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

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.squareup.picasso.Picasso;

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
                    Firebase f = new Firebase(Constants.BASE_URL);
                    final ProfilePacient p = getFieldsProfile();
                    f.child("users").child(f.getAuth().getUid()).child("profile").setValue(p, new Firebase.CompletionListener() {
                        @Override
                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                            if (firebaseError == null) {
                                showSnackBar("Perfil Atualizado");
                                Singleton.getInstance().setPp(p);
                            } else {
                                showSnackBar("Erro ao atualizar perfil. Tente novamente");
                            }
                        }
                    });
                    Utils.setViewAndChildrenEnabled(v, false);
                    getActivity().invalidateOptionsMenu();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.imgProfile})
    public void OnClick() {
        Utils.openCamera(getActivity());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.CAMERA_INTENT) {
            if (resultCode == Activity.RESULT_OK) {
                Log.i("Aqui", "Aqui");
                Picasso.with(getActivity()).load(data.getData()).into(imgProfile);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
}
