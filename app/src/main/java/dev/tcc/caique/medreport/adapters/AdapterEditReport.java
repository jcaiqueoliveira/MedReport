package dev.tcc.caique.medreport.adapters;

import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import dev.tcc.caique.medreport.R;
import dev.tcc.caique.medreport.models.Image;

/**
 * Created by Avell B153 MAX on 30/04/2016.
 */
public class AdapterEditReport extends BaseAdapter {
    private ArrayList<Image> urls;
    private FragmentActivity mContext;

    public AdapterEditReport(ArrayList<Image> urls, FragmentActivity mContex) {
        this.urls = urls;
        this.mContext = mContex;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(5, 5, 5, 5);
        } else {
            imageView = (ImageView) convertView;
        }

        Glide.with(mContext).load(urls.get(position).getImage()).placeholder(android.R.drawable.progress_indeterminate_horizontal).into(imageView);
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Deseja remover a imagem?");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        urls.remove(position);
                        notifyDataSetChanged();
                        dialog.dismiss();

                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                return false;
            }
        });
        return imageView;
    }
}