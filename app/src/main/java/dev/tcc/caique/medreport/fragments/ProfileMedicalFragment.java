package dev.tcc.caique.medreport.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import dev.tcc.caique.medreport.models.ProfileMedical;
import dev.tcc.caique.medreport.models.Singleton;
import dev.tcc.caique.medreport.utils.Constants;
import dev.tcc.caique.medreport.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */

public class ProfileMedicalFragment extends Fragment {
    @Bind(R.id.imgProfile)
    CircleImageView imgProfile;
    @Bind(R.id.nameMedical)
    EditText nameMedical;
    @Bind(R.id.specialization)
    EditText specialization;
    @Bind(R.id.crm)
    EditText crm;
    private boolean isEditing;
    private View v;
    @Bind(R.id.textNameMedical)
    TextInputLayout textNameMedical;

    public ProfileMedicalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_profile_medical, container, false);
        ButterKnife.bind(this, v);
        setHasOptionsMenu(true);
        nameMedical.setText(Singleton.getInstance().getName());
        crm.setText(Singleton.getInstance().getPm().getCrm()==null?"":Singleton.getInstance().getPm().getCrm());
        specialization.setText(Singleton.getInstance().getPm().getSpecialization()==null?"":Singleton.getInstance().getPm().getSpecialization());
        Utils.setViewAndChildrenEnabled(v, false);
        ((MainActivity) getActivity()).fab.hide();
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
                if (TextUtils.isEmpty(nameMedical.getText().toString())) {
                    textNameMedical.setError("Campo obrigatório");
                } else {
                    updateProfileInformationsFirebase();
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
                Picasso.with(getActivity()).load(data.getData()).into(imgProfile);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateProfileInformationsFirebase() {
        final ProfileMedical profileMedical = new ProfileMedical();
        profileMedical.setName(nameMedical.getText().toString());
        profileMedical.setCrm(crm.getText().toString());
        profileMedical.setSpecialization(specialization.getText().toString());
        Firebase f = new Firebase(Constants.BASE_URL);
        f.child("users").child(f.getAuth().getUid()).child("profile").setValue(profileMedical, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError == null) {
                    showSnackBar("Perfil Atualizado");
                    Singleton.getInstance().setPm(profileMedical);
                } else {
                    showSnackBar("Erro ao atualizar perfil. Tente novamente");
                }
            }
        });
    }

    private void showSnackBar(String msg) {
        Snackbar snack = Snackbar.make(v.findViewById(R.id.parent), msg, Snackbar.LENGTH_LONG);
        View view = snack.getView();
        ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(ContextCompat.getColor(getActivity(), android.R.color.white));
        snack.show();
    }
}
