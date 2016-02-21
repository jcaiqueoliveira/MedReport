package dev.tcc.caique.medreport.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dev.tcc.caique.medreport.R;
import dev.tcc.caique.medreport.activities.MainActivity;
import dev.tcc.caique.medreport.adapters.InviteAdapter;
import dev.tcc.caique.medreport.adapters.ReportAdapter;
import dev.tcc.caique.medreport.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class InviteFragment extends Fragment {


    public InviteFragment() {
        // Required empty public constructor
    }

    private RecyclerView recyclerView;
    private InviteAdapter inviteAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_invite, container, false);
        ((MainActivity)getActivity()).fab.show();
        recyclerView = (RecyclerView) v.findViewById(R.id.inviteRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        inviteAdapter = new InviteAdapter();
        recyclerView.setAdapter(inviteAdapter);
        return v;
    }
    @Override
    public void onResume() {
        ((MainActivity) getActivity()).navigationView.setCheckedItem(Constants.INVITE);
        super.onResume();
    }
}
