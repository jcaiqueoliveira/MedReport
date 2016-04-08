package dev.tcc.caique.medreport.fragments;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import dev.tcc.caique.medreport.R;
import dev.tcc.caique.medreport.models.Report;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowReportMedicalFragment extends Fragment {

    @Bind(R.id.title)
    EditText title;
    @Bind(R.id.description)
    EditText description;
    @Bind(R.id.inputDescription)
    TextInputLayout inputDescription;
    @Bind(R.id.inputTitle)
    TextInputLayout inputTitle;

    public ShowReportMedicalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_show_report_medical, container, false);
        ButterKnife.bind(this, v);
        Bundle b = getArguments();
        if (b != null) {
            Report report = (Report) b.getSerializable("report");
            if (report != null) {
                title.setText(report.getTitle());
                description.setText(report.getDescription());
            }
        }
        return v;
    }

}
