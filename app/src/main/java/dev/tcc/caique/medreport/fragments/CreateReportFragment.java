package dev.tcc.caique.medreport.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dev.tcc.caique.medreport.R;
import dev.tcc.caique.medreport.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateReportFragment extends Fragment {


    public CreateReportFragment() {
        // Required empty public constructor
    }

    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_create_report, container, false);
        ((MainActivity) getActivity()).fab.hide();
        return v;
    }

}
