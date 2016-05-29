package dev.tcc.caique.medreport.fragments;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import dev.tcc.caique.medreport.R;
import dev.tcc.caique.medreport.activities.MainActivity;
import dev.tcc.caique.medreport.adapters.AdapterEditReport;
import dev.tcc.caique.medreport.models.Image;
import dev.tcc.caique.medreport.models.Report;
import dev.tcc.caique.medreport.models.Singleton;
import dev.tcc.caique.medreport.utils.Constants;

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
    @Bind(R.id.gridview)
    GridView gridView;

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
                loadImagesReport(b.getString("uid"), report);
            }
        }
        return v;
    }


    private void loadImagesReport(String uuid, final Report r) {
        final ArrayList<Image> images = new ArrayList<>();
        Firebase f = new Firebase(Constants.BASE_URL + "/images");
        f.child(r.getStackId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("resuult2", dataSnapshot.toString());
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Log.e("Resul", "Cadê" + ds.toString());
                    images.add(ds.getValue(Image.class));
                }
                Singleton.getInstance().setCurrentImageInReport(images);
                gridView.setAdapter(new AdapterEditReport(getActivity(), r, true));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public void onResume() {
        ((MainActivity) getActivity()).navigationView.setCheckedItem(Constants.REPORT);
        ((MainActivity)getActivity()).toolbar.setTitle("Relatórios");
        super.onResume();
    }
}
