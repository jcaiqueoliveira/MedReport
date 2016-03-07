package dev.tcc.caique.medreport.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import dev.tcc.caique.medreport.R;
import dev.tcc.caique.medreport.activities.MainActivity;
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
        ButterKnife.bind(this,v);
        setHasOptionsMenu(true);
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
                Utils.setViewAndChildrenEnabled(v,false);
                getActivity().invalidateOptionsMenu();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @OnClick({R.id.imgProfile})
    public void OnClick(){
        Utils.openCamera(getActivity());
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.CAMERA_INTENT){
            if(resultCode == Activity.RESULT_OK){
                Log.i("Aqui", "Aqui");
                Picasso.with(getActivity()).load(data.getData()).into(imgProfile);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
