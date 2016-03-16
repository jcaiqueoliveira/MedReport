package dev.tcc.caique.medreport.adapters;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import dev.tcc.caique.medreport.R;

/**
 * Created by caique on 30/01/16.
 */
public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolderReport>  {

    @Override
    public ViewHolderReport onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderReport(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_report_card_list,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolderReport holder, int position) {
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class ViewHolderReport extends RecyclerView.ViewHolder{
    public ImageView delete,edit, send;
        public TextView nameReport;
    public ViewHolderReport(View v) {
        super(v);
        delete = (ImageView)v.findViewById(R.id.delete);
        edit   = (ImageView)v.findViewById(R.id.edit);
        send   = (ImageView)v.findViewById(R.id.send);
        nameReport = (TextView) v.findViewById(R.id.nameReport);
    }
}
}
