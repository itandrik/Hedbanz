package com.transcendensoft.hedbanz.view.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.model.data.PreferenceManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartActivity extends AppCompatActivity {
    @BindView(R.id.ivHatImage) ImageView mIvHat;
    @BindView(R.id.ivSmileImage) ImageView mIvSmile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this, this);

        if(new PreferenceManager(this).isAuthorised()){
            //TODO go to main activity
        } else {
            initSmileAnimation();
        }
    }

    private void initSmileAnimation(){
        ViewTreeObserver vto = mIvSmile.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mIvSmile.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        mIvSmile.getWidth(), mIvSmile.getWidth());
                mIvSmile.setLayoutParams(params);

                Drawable drawable = AnimatedVectorDrawableCompat.create(
                        StartActivity.this, R.drawable.anim_smile);
                mIvSmile.setImageDrawable(drawable);
                ((AnimatedVectorDrawableCompat) drawable).start();

                initHatAnimation();
            }
        });
    }

    private void initHatAnimation(){
        Drawable drawable = VectorDrawableCompat.create(getResources(),R.drawable.hat, null);
        mIvHat.setImageDrawable(drawable);

       // mIvHat.setTranslationY();
    }
}
