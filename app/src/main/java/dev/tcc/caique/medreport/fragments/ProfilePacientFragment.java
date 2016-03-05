package dev.tcc.caique.medreport.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import dev.tcc.caique.medreport.R;
import dev.tcc.caique.medreport.activities.MainActivity;
import dev.tcc.caique.medreport.utils.Constants;

public class ProfilePacientFragment extends Fragment {
    private boolean isEditing = false;
    @Bind(R.id.layout)
    LinearLayout layout;
    private View v;

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
        setViewAndChildrenEnabled(v, false);
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
                setViewAndChildrenEnabled(v,true);
                getActivity().invalidateOptionsMenu();
                break;
            case R.id.saveProfile:
                isEditing = false;
                setViewAndChildrenEnabled(v,false);
                getActivity().invalidateOptionsMenu();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private static void setViewAndChildrenEnabled(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                setViewAndChildrenEnabled(child, enabled);
            }
        }
    }
}
