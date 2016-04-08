package dev.tcc.caique.medreport.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import dev.tcc.caique.medreport.R;
import dev.tcc.caique.medreport.activities.MainActivity;
import dev.tcc.caique.medreport.models.Accompaniments;
import dev.tcc.caique.medreport.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragmentMedical extends Fragment {

    @Bind(R.id.accompanimentsList)
    RecyclerView accompanimentsList;
    FirebaseRecyclerAdapter<Accompaniments, ViewHolderAccompaniments> adapter;
    @Bind(R.id.noItem)
    TextView noItem;

    public ReportFragmentMedical() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_report_fragment_medical, container, false);
        ButterKnife.bind(this, v);
        ((MainActivity) getActivity()).fab.hide();
        accompanimentsList.setHasFixedSize(true);
        accompanimentsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        final Firebase ref = new Firebase(Constants.BASE_URL);
        final Firebase ref2 = new Firebase(Constants.BASE_URL + "friends/" + ref.getAuth().getUid());
        final Firebase ref3 = new Firebase(Constants.BASE_URL + "users");
        try {
            adapter = new FirebaseRecyclerAdapter<Accompaniments, ViewHolderAccompaniments>(Accompaniments.class, R.layout.layout_report_medical_item, ViewHolderAccompaniments.class, ref2) {
                @Override
                protected void populateViewHolder(final ViewHolderAccompaniments viewHolderAccompaniments, final Accompaniments accompaniments, int i) {

                    Query queryRef = ref3.orderByChild("email").equalTo(accompaniments.getEmail());
                    queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                                viewHolderAccompaniments.view.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Bundle b = new Bundle();
                                        b.putString("stack",ds.getKey());
                                        ListReportPacientOfMedicalFragment list = new ListReportPacientOfMedicalFragment();
                                        list.setArguments(b);
                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, list).addToBackStack(null).commit();
                                    }
                                });
                                ref.child("reports").child(ds.getKey()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        viewHolderAccompaniments.numberReport.setText("Relatórios: " + snapshot.getChildrenCount());
                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });
                                viewHolderAccompaniments.namePerson.setText((String) ds.child("name").getValue());
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            Log.i("error serach", firebaseError.getMessage());
                        }
                    });
                    // todo atualizar o onclick
                    viewHolderAccompaniments.view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           /* Intent i = new Intent(getActivity(), ChatActivity.class);
                            Bundle b = new Bundle();
                            b.putString("SALA", accompaniments.getChat());
                            i.putExtras(b);
                            startActivity(i);*/
                            Toast.makeText(getActivity(), "Ação a ser atualizada", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            };
            accompanimentsList.setAdapter(adapter);
            updateUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    public static class ViewHolderAccompaniments extends RecyclerView.ViewHolder {
        public CircleImageView thumbnail;
        public TextView namePerson, numberReport;
        public View view;

        public ViewHolderAccompaniments(View v) {
            super(v);
            this.view = v;
            thumbnail = (CircleImageView) v.findViewById(R.id.thumbnail);
            numberReport = (TextView) v.findViewById(R.id.numberReport);
            namePerson = (TextView) v.findViewById(R.id.nameAccompanimentList);
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
