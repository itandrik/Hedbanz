package com.transcendensoft.hedbanz.view.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.util.AndroidUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.ivSmileGif) ImageView mIvSmileGif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        ButterKnife.bind(this, this);
        int size = (int) AndroidUtils.convertDpToPixel(100, this);

        Glide.with(this).asGif().load(R.raw.smile_gif_new).preload(size, size);
    }

    @OnClick(R.id.btnRegisterSubmit)
    protected void onRegisterSubmitClicked() {
        Glide.with(this).asGif().load(R.raw.smile_gif_new)
                .into(mIvSmileGif);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.login_page_left_in, R.anim.login_page_left_out);
    }

    @OnClick(R.id.ivBack)
    protected void onBackClicked() {
        onBackPressed();
    }
}
