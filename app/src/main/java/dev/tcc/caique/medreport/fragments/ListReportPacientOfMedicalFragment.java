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
import dev.tcc.caique.medreport.models.Report;
import dev.tcc.caique.medreport.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListReportPacientOfMedicalFragment extends Fragment {


    public ListReportPacientOfMedicalFragment() {
        // Required empty public constructor
    }

    private Firebase ref;
    private Firebase ref2;
    private RecyclerView recyclerView;
    FirebaseRecyclerAdapter<Report, ViewHolderReport> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list_report_pacient_of_medical, container, false);
        Bundle b = getArguments();
        if (b != null) {
            final String stack = b.getString("stack");
            ref = new Firebase(Constants.BASE_URL + "reports");
            ref2 = ref.child(stack);
            recyclerView = (RecyclerView) v.findViewById(R.id.reportRecyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter = new FirebaseRecyclerAdapter<Report, ViewHolderReport>(Report.class,
                    R.layout.layout_report_card_list,
                    ViewHolderReport.class,
                    ref2) {
                @Override
                protected void populateViewHolder(ViewHolderReport viewHolderReport, final Report report, int i) {
                    viewHolderReport.nameReport.setText(report.getTitle());
                    viewHolderReport.v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle b = new Bundle();
                            b.putSerializable("report", report);
                            b.putString("uid", stack);
                            ShowReportMedicalFragment showReportMedicalFragment = new ShowReportMedicalFragment();
                            showReportMedicalFragment.setArguments(b);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, showReportMedicalFragment).addToBackStack(null).commit();
                        }
                    });
                }
            };
            recyclerView.setAdapter(adapter);
        }

        return v;
    }

    public static class ViewHolderReport extends RecyclerView.ViewHolder {
        public ImageView delete, edit, send;
        public TextView nameReport;
        public View v;

        public ViewHolderReport(View v) {
            super(v);
            this.v = v;
            delete = (ImageView) v.findViewById(R.id.delete);
            edit = (ImageView) v.findViewById(R.id.edit);
            nameReport = (TextView) v.findViewById(R.id.nameReport);
            delete.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);
        }
    }
}
