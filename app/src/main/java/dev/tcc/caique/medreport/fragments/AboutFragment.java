package dev.tcc.caique.medreport.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.tcc.caique.medreport.R;
import dev.tcc.caique.medreport.activities.MainActivity;
import dev.tcc.caique.medreport.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {


    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this,v);
        ((MainActivity)getActivity()).fab.hide();
        return v;
    }
    @OnClick({R.id.buttonContact})
    public void onClick(){
        String uriText =
                "mailto:jcaique.jc@gmail.com;andcarv@gmail.com;wilder_roberto@hotmail.com"+
                        "?subject=" + Uri.encode("MedReport Contact");

        Uri uri = Uri.parse(uriText);
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
        sendIntent.setData(uri);
        startActivity(Intent.createChooser(sendIntent, "Como deseja realizar essa ação:"));
    }
    @Override
    public void onResume() {
        ((MainActivity) getActivity()).navigationView.setCheckedItem(Constants.ABOUT);
        ((MainActivity)getActivity()).toolbar.setTitle("Sobre");
        super.onResume();
    }
}
