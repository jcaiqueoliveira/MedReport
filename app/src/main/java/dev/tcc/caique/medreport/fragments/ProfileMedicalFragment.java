package dev.tcc.caique.medreport.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import dev.tcc.caique.medreport.R;
import dev.tcc.caique.medreport.activities.MainActivity;
import dev.tcc.caique.medreport.utils.Constants;
import dev.tcc.caique.medreport.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */

public class ProfileMedicalFragment extends Fragment {
    @Bind(R.id.imgProfile)
    CircleImageView imgProfile;
    private boolean isEditing;
    private View v;

    public ProfileMedicalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_profile_medical, container, false);
        ButterKnife.bind(this,v);
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
                Utils.setViewAndChildrenEnabled(v, false);
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
                Picasso.with(getActivity()).load(data.getData()).into(imgProfile);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
