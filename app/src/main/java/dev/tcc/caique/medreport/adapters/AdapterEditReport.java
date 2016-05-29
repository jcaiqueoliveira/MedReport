package dev.tcc.caique.medreport.adapters;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;

import dev.tcc.caique.medreport.R;
import dev.tcc.caique.medreport.activities.ShowImage;
import dev.tcc.caique.medreport.models.Image;
import dev.tcc.caique.medreport.models.Report;
import dev.tcc.caique.medreport.models.Singleton;
import dev.tcc.caique.medreport.service.ServiceDeleImageCloudinary;
import dev.tcc.caique.medreport.utils.Constants;

/**
 * Caique Oliveira on 30/04/2016.
 */
public class AdapterEditReport extends BaseAdapter {
    private FragmentActivity mContext;
    private Report report;
    private boolean isOnlyShow = false;

    public AdapterEditReport(FragmentActivity mContex, Report report) {
        this.mContext = mContex;
        this.report = report;
    }

    public AdapterEditReport(FragmentActivity mContext, Report report, boolean isOnlyShow) {
        Log.e("Aqui", "aqui");
        this.mContext = mContext;
        this.report = report;
        this.isOnlyShow = isOnlyShow;
    }

    @Override
    public int getCount() {
        return Singleton.getInstance().getCurrentImageInReport().size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(5, 5, 5, 5);
            imageView.setId(R.id.showImage);
            imageView.setTransitionName("exibicao");
        } else {
            imageView = (ImageView) convertView;
        }

        Glide.with(mContext).load(Singleton.getInstance().getCurrentImageInReport().get(position).getImage()).placeholder(android.R.drawable.progress_indeterminate_horizontal).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(mContext, imageView, "exibicao");
                Singleton.getInstance().setUrlToShow(Singleton.getInstance().getCurrentImageInReport().get(position).getImage());
                mContext.startActivity(new Intent(mContext, ShowImage.class), options.toBundle());
            }
        });

        if (!isOnlyShow) {
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