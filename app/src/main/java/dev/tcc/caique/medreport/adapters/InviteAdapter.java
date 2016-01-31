package dev.tcc.caique.medreport.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import dev.tcc.caique.medreport.R;

/**
 * Created by caique on 30/01/16.
 */
public class InviteAdapter extends RecyclerView.Adapter<InviteAdapter.ViewHolderInvite>  {

    @Override
    public ViewHolderInvite onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderInvite(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_invite_card_list, null));
    }

    @Override
    public void onBindViewHolder(ViewHolderInvite holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class ViewHolderInvite extends RecyclerView.ViewHolder{
        public ImageView accept,exclude;
        public ViewHolderInvite(View v) {
            super(v);
        }
    }
}
