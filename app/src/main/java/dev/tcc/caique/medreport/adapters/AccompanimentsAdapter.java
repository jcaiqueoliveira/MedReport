package dev.tcc.caique.medreport.adapters;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import dev.tcc.caique.medreport.R;
import dev.tcc.caique.medreport.activities.ChatActivity;
import dev.tcc.caique.medreport.models.Accompaniments;

/**
 * Created by Avell B153 MAX on 21/02/2016.
 */
public class AccompanimentsAdapter extends RecyclerView.Adapter<AccompanimentsAdapter.ViewHolderAccompaniments> {
    private FragmentActivity context;
    private ArrayList<Accompaniments> accompanimentses;

    public AccompanimentsAdapter(FragmentActivity context) {
        this.context = context;
    }

    @Override
    public ViewHolderAccompaniments onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderAccompaniments(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_accompaniments, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolderAccompaniments holder, final int position) {
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ChatActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("USER", new Accompaniments());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class ViewHolderAccompaniments extends RecyclerView.ViewHolder {
        public CircleImageView thumbnail;
        public TextView nameAccompanimentList;
        public View v;

        public ViewHolderAccompaniments(View v) {
            super(v);
            this.v = v;
            thumbnail = (CircleImageView) v.findViewById(R.id.thumbnail);
            nameAccompanimentList = (TextView) v.findViewById(R.id.nameAccompanimentList);
            nameAccompanimentList = (TextView) v.findViewById(R.id.nameAccompanimentList);

        }
    }
}
