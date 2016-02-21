package dev.tcc.caique.medreport.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dev.tcc.caique.medreport.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccompanimentsFragment extends Fragment {


    public AccompanimentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_accompaniments, container, false);
    }

}
