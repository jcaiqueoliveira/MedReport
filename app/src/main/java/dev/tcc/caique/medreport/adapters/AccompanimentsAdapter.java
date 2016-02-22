package dev.tcc.caique.medreport.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import dev.tcc.caique.medreport.R;

/**
 * Created by Avell B153 MAX on 21/02/2016.
 */
public class AccompanimentsAdapter extends RecyclerView.Adapter<AccompanimentsAdapter.ViewHolderAccompaniments> {
    @Override
    public ViewHolderAccompaniments onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderAccompaniments(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_accompaniments, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolderAccompaniments holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class ViewHolderAccompaniments extends RecyclerView.ViewHolder {
        public ImageView accept, exclude;

        public ViewHolderAccompaniments(View v) {
            super(v);
        }
    }

}
