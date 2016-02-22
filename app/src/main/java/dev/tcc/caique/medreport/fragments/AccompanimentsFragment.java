package dev.tcc.caique.medreport.fragments;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import dev.tcc.caique.medreport.R;
import dev.tcc.caique.medreport.activities.MainActivity;
import dev.tcc.caique.medreport.adapters.AccompanimentsAdapter;
import dev.tcc.caique.medreport.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccompanimentsFragment extends Fragment {
    @Bind(R.id.accompanimentsList)
    RecyclerView accompanimentsList;
    private AccompanimentsAdapter accompanimentsAdapter;
    public AccompanimentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_accompaniments, container, false);
        ButterKnife.bind(this,v);
        ((MainActivity) getActivity()).fab.hide();
        accompanimentsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        accompanimentsAdapter = new AccompanimentsAdapter();
        accompanimentsList.setAdapter(accompanimentsAdapter);
        getAccounts();
        return v;
    }

    @Override
    public void onResume() {
        ((MainActivity) getActivity()).navigationView.setCheckedItem(Constants.ACCOMPANIMENTS);
        super.onResume();
    }

    private void getAccounts(){
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(getContext()).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                String possibleEmail = account.name;
                String type = account.type;
                Log.i("Email", possibleEmail);
                Log.i("Type", type);
            }
        }
    }
}