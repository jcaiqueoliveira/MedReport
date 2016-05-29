package dev.tcc.caique.medreport.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;
import dev.tcc.caique.medreport.R;
import dev.tcc.caique.medreport.models.Singleton;

public class ShowImage extends AppCompatActivity {
    @Bind(R.id.showImage)
    ImageView showImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        ButterKnife.bind(this);
        Glide.with(this).load(Singleton.getInstance().getUrlToShow()).into(showImage);
    }

    @Override
    protected void onDestroy() {
        this.supportFinishAfterTransition();
        super.onDestroy();
    }
}
