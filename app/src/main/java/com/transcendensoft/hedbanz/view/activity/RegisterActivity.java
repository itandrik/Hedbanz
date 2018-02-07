package com.transcendensoft.hedbanz.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.model.entity.User;
import com.transcendensoft.hedbanz.presenter.PresenterManager;
import com.transcendensoft.hedbanz.presenter.impl.RegisterPresenterImpl;
import com.transcendensoft.hedbanz.util.AndroidUtils;
import com.transcendensoft.hedbanz.view.RegisterView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;

public class RegisterActivity extends AppCompatActivity implements RegisterView {
    @BindView(R.id.ivSmileGif) ImageView mIvSmileGif;
    @BindView(R.id.etLogin) EditText mEtLogin;
    @BindView(R.id.etEmail) EditText mEtEmail;
    @BindView(R.id.etPassword) EditText mEtPassword;
    @BindView(R.id.etConfirmPassword) EditText mEtConfirmPassword;
    @BindView(R.id.tvErrorLogin) TextView mTvLoginError;
    @BindView(R.id.tvErrorEmail) TextView mTvEmailError;
    @BindView(R.id.tvErrorPassword) TextView mTvPasswordError;
    @BindView(R.id.tvErrorConfirmPassword) TextView mTvConfirmPasswordError;

    private ProgressDialog mProgressDialog;
    private RegisterPresenterImpl mPresenter;
    private Socket mSocket;

    /*------------------------------------*
     *-------- Activity lifecycle --------*
     *------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources()
                    .getColor(R.color.colorPrimaryLight));
        }
        //AndroidUtils.makeStatusBarTranslucent(this);

        setContentView(R.layout.activity_register);
        ButterKnife.bind(this, this);

        int size = (int) AndroidUtils.convertDpToPixel(100, this);
        Glide.with(this).asGif().load(R.raw.smile_gif_new).preload(size, size);

        initProgressDialog();
        initPresenter(savedInstanceState);
        initSocket();
        //initEditTextListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.bindView(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.unbindView();
            mSocket.disconnect();
            mSocket.off("loginResult", onLoginResultListener);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        PresenterManager.getInstance().savePresenter(mPresenter, outState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.login_page_left_in, R.anim.login_page_left_out);
    }

    @Override
    public Context provideContext() {
        return this;
    }

    /*------------------------------------*
     *---------- Initialization ----------*
     *------------------------------------*/
    private void initPresenter(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mPresenter = new RegisterPresenterImpl();
        } else {
            mPresenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }
    }

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.action_loading));
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
    }

    private void initEditTextListeners() {
        mPresenter.initAnimEditTextListener(mEtEmail);
        mPresenter.initAnimEditTextListener(mEtLogin);
        mPresenter.initAnimEditTextListener(mEtPassword);
        mPresenter.initAnimEditTextListener(mEtConfirmPassword);
    }

    private void initSocket() {
        try {
            mSocket = IO.socket("http://77.47.204.201:9092/socket");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        initSocketListeners();
        mSocket.on("loginResult", onLoginResultListener);
        mSocket.connect();
    }

    private Emitter.Listener onLoginResultListener;

    private void initSocketListeners() {
        onLoginResultListener = args -> {
            runOnUiThread(() -> {
                JSONObject data = (JSONObject) args[0];
                boolean isLoginAvaliable;
                try {
                    isLoginAvaliable = data.getBoolean("isLoginAvailable");
                } catch (JSONException e) {
                    Log.e("TAG", e.getMessage());
                    return;
                }
                AndroidUtils.showShortToast(this, "Is login available : " + isLoginAvaliable);
            });
        };

        RxTextView.textChanges(mEtLogin).debounce(300, TimeUnit.MILLISECONDS)
                .filter(text -> text != null && !TextUtils.isEmpty(text))
                .subscribe(text -> {
                    if (mSocket != null && mSocket.connected()) {
                        mSocket.emit("checkLogin", text.toString().trim());
                    }
                });
    }

    /*------------------------------------*
     *--------- Set data to view ---------*
     *------------------------------------*/
    @Override
    public void registerSuccess() {
        AndroidUtils.showShortToast(this, "Login success");
    }

    @Override
    public void startSmileAnimation() {
        Glide.with(this).asGif().load(R.raw.smile_gif_new).into(mIvSmileGif);
    }

    @Override
    public void stopSmileAnimation() {
        Glide.with(this).load(R.drawable.logo).into(mIvSmileGif);
    }

    /*------------------------------------*
     *-------- On click listeners --------*
     *------------------------------------*/
    @OnClick(R.id.btnRegisterSubmit)
    protected void onRegisterSubmitClicked() {
        hideAll();

        User user = new User.Builder()
                .setEmail(mEtEmail.getText().toString())
                .setLogin(mEtLogin.getText().toString())
                .setPassword(mEtPassword.getText().toString())
                .setConfirmPassword(mEtConfirmPassword.getText().toString())
                .build();

        mPresenter.registerUser(user);
    }

    @OnClick(R.id.ivBack)
    protected void onBackClicked() {
        onBackPressed();
    }

    /*------------------------------------*
     *-------- Error and loading ---------*
     *------------------------------------*/
    @Override
    public void showIncorrectLogin(int message) {
        mTvLoginError.setText(getString(message));
        mTvLoginError.setVisibility(View.VISIBLE);
    }

    @Override
    public void showIncorrectEmail(int message) {
        mTvEmailError.setText(getString(message));
        mTvEmailError.setVisibility(View.VISIBLE);
    }

    @Override
    public void showIncorrectPassword(int message) {
        mTvPasswordError.setText(getString(message));
        mTvPasswordError.setVisibility(View.VISIBLE);
    }

    @Override
    public void showIncorrectConfirmPassword(int message) {
        mTvConfirmPasswordError.setText(getString(message));
        mTvConfirmPasswordError.setVisibility(View.VISIBLE);
    }

    @Override
    public void showServerError() {
        hideAll();
        AndroidUtils.showShortToast(this, R.string.error_server);
    }

    @Override
    public void showNetworkError() {
        hideAll();
        AndroidUtils.showShortToast(this, R.string.error_network);
    }

    @Override
    public void showLoading() {
        hideAll();
        if (mProgressDialog != null) {
            mProgressDialog.show();
        }
    }

    @Override
    public void showContent() {
        // Stub
    }

    private void hideAll() {
        hideLoading();
        mTvConfirmPasswordError.setVisibility(GONE);
        mTvEmailError.setVisibility(GONE);
        mTvLoginError.setVisibility(GONE);
        mTvPasswordError.setVisibility(GONE);
    }

    private void hideLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.hide();
        }
    }
}
