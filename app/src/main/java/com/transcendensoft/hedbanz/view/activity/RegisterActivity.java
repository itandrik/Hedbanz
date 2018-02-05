package com.transcendensoft.hedbanz.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.util.AndroidUtils;
import com.transcendensoft.hedbanz.view.RegisterView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity implements RegisterView {
    @BindView(R.id.ivSmileGif) ImageView mIvSmileGif;

    /*------------------------------------*
     *-------- Activity lifecycle --------*
     *------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        AndroidUtils.makeStatusBarTranslucent(this);
        ButterKnife.bind(this, this);

        int size = (int) AndroidUtils.convertDpToPixel(100, this);
        Glide.with(this).asGif().load(R.raw.smile_gif_new).preload(size, size);
    }

    @OnClick(R.id.btnRegisterSubmit)
    protected void onRegisterSubmitClicked() {
        //Glide.with(this).asGif().load(R.raw.smile_gif_new)
         //       .into(mIvSmileGif);
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

    /*------------------------------------*
     *--------- Set data to view ---------*
     *------------------------------------*/
    @Override
    public void registerSuccess() {

    }

    /*------------------------------------*
     *-------- Error and loading ---------*
     *------------------------------------*/
    @Override
    public void showIncorrectLogin(String message) {

    }

    @Override
    public void showIncorrectEmail(String message) {

    }

    @Override
    public void showIncorrectPassword(String message) {

    }

    @Override
    public void showIncorrectConfirmPassword(String message) {

    }

    private void hideAll(){

    }
}
