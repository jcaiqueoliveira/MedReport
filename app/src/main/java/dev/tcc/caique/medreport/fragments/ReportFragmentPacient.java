package dev.tcc.caique.medreport.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import dev.tcc.caique.medreport.R;
import dev.tcc.caique.medreport.activities.MainActivity;
import dev.tcc.caique.medreport.models.Image;
import dev.tcc.caique.medreport.models.Report;
import dev.tcc.caique.medreport.models.Singleton;
import dev.tcc.caique.medreport.service.ServiceDeleImageCloudinary;
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
                protected void populateViewHolder(ViewHolderReport viewHolderReport, final Report r, int i) {
                    noItem.setVisibility(View.GONE);
                    viewHolderReport.nameReport.setText(r.getTitle());
                    Firebase firebase1 = new Firebase(Constants.BASE_URL + "images/" + r.getStackId());
                    firebase1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                Log.i("dataSnap", ds.toString());
                                //images.add(ds.getValue(Image.class));
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    viewHolderReport.delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            confirmDeleteReport(r);
                        }
                    });
                    viewHolderReport.edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle b = new Bundle();
                            b.putSerializable("report", r);
                            CreateReportFragment createReportFragment = new CreateReportFragment();
                            createReportFragment.setArguments(b);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, createReportFragment).addToBackStack(null).commit();
                        }
                    });
                }
            };
            recyclerView.setAdapter(adapter);
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
        public ImageView delete, edit;
        public TextView nameReport;

        public ViewHolderReport(View v) {
            super(v);
            delete = (ImageView) v.findViewById(R.id.delete);
            edit = (ImageView) v.findViewById(R.id.edit);
            nameReport = (TextView) v.findViewById(R.id.nameReport);
        }
    }

    private void confirmDeleteReport(final Report r) {
        android.support.v7.app.AlertDialog alertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Apagar relatório");
        builder.setMessage("Deseja realmente apagar este relatório?");
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteReport(r);
                dialog.dismiss();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
    }

    private void deleteReport(final Report r) {
        ref.child(ref.getAuth().getUid()).child(r.getStackId()).removeValue(new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError == null) {
                    final ArrayList<Image> images = new ArrayList<>();
                    Firebase firebase1 = new Firebase(Constants.BASE_URL + "images/" + r.getStackId());
                    firebase1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                images.add(ds.getValue(Image.class));
                            }
                            Singleton.getInstance().setImageToDeleteCloudinary(images);
                            getActivity().startService(new Intent(getActivity(), ServiceDeleImageCloudinary.class));
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    firebase1.removeValue(new Firebase.CompletionListener() {
                        @Override
                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                            if (firebaseError == null) {
                                Snackbar.make(((MainActivity) getActivity()).fab, "Removido com sucesso", Snackbar.LENGTH_SHORT).show();
                                if (adapter != null && adapter.getItemCount() == 0) {
                                    noItem.setVisibility(View.VISIBLE);
                                } else {
                                    noItem.setVisibility(View.GONE);
                                }
                            } else {
                                Snackbar.make(((MainActivity) getActivity()).fab, "Erro, tente novamente", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Erro ao excluir. Tente Novamente", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
