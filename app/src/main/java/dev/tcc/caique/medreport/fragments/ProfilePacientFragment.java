package dev.tcc.caique.medreport.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dev.tcc.caique.medreport.R;

public class ProfilePacientFragment extends Fragment {

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
        View v = inflater.inflate(R.layout.fragment_profile_pacient, container, false);
        return v;
    }
}
