package dev.tcc.caique.medreport.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import dev.tcc.caique.medreport.activities.ChatActivity;
import dev.tcc.caique.medreport.activities.MainActivity;
import dev.tcc.caique.medreport.models.Accompaniments;
import dev.tcc.caique.medreport.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccompanimentsFragment extends Fragment {
    @Bind(R.id.accompanimentsList)
    RecyclerView accompanimentsList;
    FirebaseRecyclerAdapter<Accompaniments, ViewHolderAccompaniments> adapter;

    public AccompanimentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_accompaniments, container, false);
        ButterKnife.bind(this, v);
        ((MainActivity) getActivity()).fab.hide();
        accompanimentsList.setHasFixedSize(true);
        accompanimentsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        final Firebase ref = new Firebase("https://medreportapp.firebaseio.com/");
        final Firebase ref2 = new Firebase("https://medreportapp.firebaseio.com/friends/" + ref.getAuth().getUid());
        final Firebase ref3 = new Firebase("https://medreportapp.firebaseio.com/users");
        try {
            adapter = new FirebaseRecyclerAdapter<Accompaniments, ViewHolderAccompaniments>(Accompaniments.class, R.layout.layout_accompaniments, ViewHolderAccompaniments.class, ref2) {
                @Override
                protected void populateViewHolder(final ViewHolderAccompaniments viewHolderAccompaniments, final Accompaniments accompaniments, int i) {
                    Query queryRef = ref3.orderByChild(accompaniments.getStackId()).equalTo(accompaniments.getStackId());
                    queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            viewHolderAccompaniments.namePerson.setText(accompaniments.getStackId());
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            Log.i("error serach", firebaseError.getMessage());
                        }
                    });

                    viewHolderAccompaniments.view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(getActivity(), ChatActivity.class);
                            Bundle b = new Bundle();
                            b.putString("SALA", accompaniments.getChat());
                            i.putExtras(b);
                            startActivity(i);
                        }
                    });
                }
            };
            accompanimentsList.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    @Override
    public void onResume() {
        ((MainActivity) getActivity()).navigationView.setCheckedItem(Constants.ACCOMPANIMENTS);
        super.onResume();
    }

    public static class ViewHolderAccompaniments extends RecyclerView.ViewHolder {
        public CircleImageView thumbnail;
        public TextView namePerson;
        public View view;

        public ViewHolderAccompaniments(View v) {
            super(v);
            this.view = v;
            thumbnail = (CircleImageView) v.findViewById(R.id.thumbnail);
            namePerson = (TextView) v.findViewById(R.id.nameAccompanimentList);
        }
    }
}
