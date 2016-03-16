package dev.tcc.caique.medreport.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseRecyclerAdapter;

import dev.tcc.caique.medreport.R;
import dev.tcc.caique.medreport.activities.MainActivity;
import dev.tcc.caique.medreport.adapters.ReportAdapter;
import dev.tcc.caique.medreport.models.Report;
import dev.tcc.caique.medreport.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragment extends Fragment {


    public ReportFragment() {
        // Required empty public constructor
    }

    private RecyclerView recyclerView;
    private ReportAdapter reportAdapter;
    private Firebase ref;
    FirebaseRecyclerAdapter<Report, ViewHolderReport> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_report, container, false);
        ((MainActivity) getActivity()).fab.show();
        ((MainActivity) getActivity()).fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new CreateReportFragment()).addToBackStack(null).commit();
            }
        });
        ref = new Firebase("https://medreportapp.firebaseio.com/").child("users").child("52a1cba1-a170-49ca-891d-65ae3a38d84f").child("report");
        recyclerView = (RecyclerView) v.findViewById(R.id.reportRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        try {
            adapter = new FirebaseRecyclerAdapter<Report, ViewHolderReport>(Report.class,
                    R.layout.layout_report_card_list,
                    ViewHolderReport.class,
                    ref) {
                @Override
                protected void populateViewHolder(ViewHolderReport viewHolderReport, Report r, int i) {
                    viewHolderReport.nameReport.setText(r.getTitle());
                }
            };
            recyclerView.setAdapter(adapter);
        }catch (Exception e){
            e.printStackTrace();
        }
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        ((MainActivity) getActivity()).navigationView.setCheckedItem(Constants.REPORT);
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.cleanup();
    }

    public static class ViewHolderReport extends RecyclerView.ViewHolder {
        public ImageView delete, edit, send;
        public TextView nameReport;

        public ViewHolderReport(View v) {
            super(v);
            delete = (ImageView) v.findViewById(R.id.delete);
            edit = (ImageView) v.findViewById(R.id.edit);
            send = (ImageView) v.findViewById(R.id.send);
            nameReport = (TextView) v.findViewById(R.id.nameReport);
        }
    }
}
