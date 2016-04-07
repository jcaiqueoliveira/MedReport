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

import butterknife.Bind;
import butterknife.ButterKnife;
import dev.tcc.caique.medreport.R;
import dev.tcc.caique.medreport.activities.MainActivity;
import dev.tcc.caique.medreport.models.Report;
import dev.tcc.caique.medreport.models.Singleton;
import dev.tcc.caique.medreport.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragmentPacient extends Fragment {


    public ReportFragmentPacient() {
        // Required empty public constructor
    }

    @Bind(R.id.noItem)
    TextView noItem;
    private RecyclerView recyclerView;
    private Firebase ref;
    private Firebase ref2;
    FirebaseRecyclerAdapter<Report, ViewHolderReport> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_report, container, false);
        ButterKnife.bind(this, v);
        if (Singleton.getInstance().getType().equals("2")) {
            ((MainActivity) getActivity()).fab.show();
            ((MainActivity) getActivity()).fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new CreateReportFragment()).addToBackStack(null).commit();
                }
            });
        } else {
            ((MainActivity) getActivity()).fab.hide();
        }

        ref = new Firebase(Constants.BASE_URL + "reports");
        ref2 = ref.child(ref.getAuth().getUid());
        recyclerView = (RecyclerView) v.findViewById(R.id.reportRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        try {
            adapter = new FirebaseRecyclerAdapter<Report, ViewHolderReport>(Report.class,
                    R.layout.layout_report_card_list,
                    ViewHolderReport.class,
                    ref2) {
                @Override
                protected void populateViewHolder(ViewHolderReport viewHolderReport, Report r, int i) {
                    viewHolderReport.nameReport.setText(r.getTitle());
                }
            };
            recyclerView.setAdapter(adapter);
            updateUI();
        } catch (Exception e) {
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
            nameReport = (TextView) v.findViewById(R.id.nameReport);
        }
    }

    public void updateUI() {
        if (adapter.getItemCount() == 0) {
            noItem.setVisibility(View.VISIBLE);
        } else {
            noItem.setVisibility(View.GONE);
        }
    }
}
