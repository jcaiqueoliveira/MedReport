package dev.tcc.caique.medreport.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
 * Created by Wilder on 3/7/2016.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Bitmap> images;
    private Report report;
    public ImageAdapter(Context c, ArrayList<Bitmap> mImages,Report report) {
        mContext = c;
        images = mImages;
        this.report = report;
    }

    public int getCount() {
        return images.size() + Singleton.getInstance().getCurrentImageInReport().size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
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
        if(position < images.size()) {
            imageView.setImageBitmap(images.get(position));
            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Deseja remover a imagem?");
                    builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            images.remove(position);
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
        }else{
            Glide.with(mContext).load(Singleton.getInstance().getCurrentImageInReport().get(position-images.size()).getImage()).into(imageView);
            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Deseja remover a imagem?");
                    builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Firebase firebase = new Firebase(Constants.BASE_URL + "images/" + report.getStackId() + "/" + Singleton.getInstance().getCurrentImageInReport().get(position).getPublicId());
                            Image image = Singleton.getInstance().getCurrentImageInReport().remove(position);
                            firebase.removeValue();
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
        }
        return imageView;
    }
}
