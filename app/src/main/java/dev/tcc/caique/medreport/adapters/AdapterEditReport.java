package dev.tcc.caique.medreport.adapters;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;

import java.util.ArrayList;

import dev.tcc.caique.medreport.models.Image;
import dev.tcc.caique.medreport.models.Report;
import dev.tcc.caique.medreport.models.Singleton;
import dev.tcc.caique.medreport.service.ServiceDeleImageCloudinary;
import dev.tcc.caique.medreport.utils.Constants;

/**
 * Created by Avell B153 MAX on 30/04/2016.
 */
public class AdapterEditReport extends BaseAdapter {
    private ArrayList<Image> urls;
    private FragmentActivity mContext;
    private Report report;

    public AdapterEditReport(ArrayList<Image> urls, FragmentActivity mContex, Report report) {
        this.urls = urls;
        this.mContext = mContex;
        this.report = report;
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
                        Firebase firebase = new Firebase(Constants.BASE_URL + "images/" + report.getStackId() + "/");
                        Image image = urls.remove(position);
                        firebase.setValue(urls);
                        Singleton.getInstance().getImageToDeleteCloudinary().clear();
                        Singleton.getInstance().getImageToDeleteCloudinary().add(image);
                        mContext.startService(new Intent(mContext, ServiceDeleImageCloudinary.class));
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