package com.transcendensoft.hedbanz.presentation.usercrud;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.presentation.base.BaseActivity;
import com.transcendensoft.hedbanz.utils.AndroidUtils;
import com.transcendensoft.hedbanz.utils.KeyboardUtils;
import com.transcendensoft.hedbanz.utils.ViewUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;

public class CredentialsActivity extends BaseActivity implements UserCrudContract.View {
    @BindView(R.id.ivSmileGif) ImageView mIvSmileGif;
    @BindView(R.id.etLogin) EditText mEtLogin;
    @BindView(R.id.etEmail) EditText mEtEmail;
    @BindView(R.id.etOldPassword) EditText mEtOldPassword;
    @BindView(R.id.etNewPassword) EditText mEtNewPassword;
    @BindView(R.id.etConfirmPassword) EditText mEtConfirmPassword;
    @BindView(R.id.tvErrorLogin) TextView mTvLoginError;
    @BindView(R.id.tvErrorEmail) TextView mTvEmailError;
    @BindView(R.id.tvErrorNewPassword) TextView mTvNewPasswordError;
    @BindView(R.id.tvErrorOldPassword) TextView mTvOldPasswordError;
    @BindView(R.id.tvErrorConfirmPassword) TextView mTvConfirmPasswordError;
    @BindView(R.id.tvLoginAvailability) TextView mTvLoginAvailability;
    @BindView(R.id.pbLoginLoading) ProgressBar mPbLoginLoading;

    @Inject UserCrudPresenter mPresenter;
    @Inject PreferenceManager mPreferenceManager;

    /*------------------------------------*
     *-------- Activity lifecycle --------*
     *------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credentials);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources()
                    .getColor(R.color.colorPrimaryLight));
        }

        ButterKnife.bind(this, this);

        int size = (int) ViewUtils.dpToPx(this, 100);
        Glide.with(this).asGif().load(R.raw.smile_gif_new).preload(size, size);

        initPasswordIcon();
        initUserData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.bindView(this);
            mPresenter.initNameCheckingListener(mEtLogin);
            initEditTextListeners();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.unbindView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.destroy();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.login_page_left_in, R.anim.login_page_left_out);
    }

    /*------------------------------------*
     *---------- Initialization ----------*
     *------------------------------------*/

    private void initEditTextListeners() {
        mPresenter.initAnimEditTextListener(mEtLogin);
        mPresenter.initAnimEditTextListener(mEtEmail);
        mPresenter.initAnimEditTextListener(mEtOldPassword);
        mPresenter.initAnimEditTextListener(mEtNewPassword);
        mPresenter.initAnimEditTextListener(mEtConfirmPassword);
    }

    private void initUserData(){
        User user = mPreferenceManager.getUser();

        mEtLogin.setText(user.getLogin());
        mEtEmail.setText(user.getEmail());
    }

    private void initPasswordIcon(){
        Drawable drawable = VectorDrawableCompat.create(
                getResources(), R.drawable.ic_password, null);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, ContextCompat.getColor(this, R.color.textDarkRed));
        mEtOldPassword.setCompoundDrawablesWithIntrinsicBounds(drawable, null,null,null);
        mEtNewPassword.setCompoundDrawablesWithIntrinsicBounds(drawable, null,null,null);
    }

    /*------------------------------------*
     *--------- Set data to view ---------*
     *------------------------------------*/
    @Override
    public void crudOperationSuccess() {
        mEtNewPassword.setText("");
        mEtConfirmPassword.setText("");
        mEtOldPassword.setText("");
        AndroidUtils.showShortToast(this, R.string.credentials_update_success);
        handleKeyboard(KeyboardUtils.KeyboardState.HIDE, null);
    }

    @Override
    public void startSmileAnimation() {
        runOnUiThread(() -> {
            Glide.with(this).asGif().load(R.raw.smile_gif_new).into(mIvSmileGif);
        });
    }

    @Override
    public void stopSmileAnimation() {
        runOnUiThread(() -> {
            Glide.with(this).load(R.drawable.logo_for_anim).into(mIvSmileGif);
        });
    }

    /*------------------------------------*
     *-------- On click listeners --------*
     *------------------------------------*/
    @OnClick(R.id.btnUpdateCredentials)
    protected void onUpdateDataClicked() {
        hideAll();

        User user = new User.Builder()
                .setEmail(mEtEmail.getText().toString())
                .setLogin(mEtLogin.getText().toString())
                .setPassword(mEtNewPassword.getText().toString())
                .setConfirmPassword(mEtConfirmPassword.getText().toString())
                .build();

        if(mPresenter != null) {
            mPresenter.updateUser(user, mEtOldPassword.getText().toString());
        }
    }

    @OnClick(R.id.ivBack)
    protected void onBackClicked() {
        onBackPressed();
    }

    /*------------------------------------*
     *-------- Error and loading ---------*
     *------------------------------------*/
    @Override
    public void showLoginAvailabilityLoading() {
        runOnUiThread(() -> {
            hideLoading();
            mTvLoginError.setVisibility(GONE);
            mPbLoginLoading.setVisibility(View.VISIBLE);
            mTvLoginAvailability.setVisibility(View.INVISIBLE);
        });
    }

    @Override
    public void hideLoginAvailability() {
        runOnUiThread(() -> {
            hideLoading();
            mTvLoginError.setVisibility(GONE);
            mTvLoginAvailability.setVisibility(View.INVISIBLE);
        });
    }

    @Override
    public void showLoginAvailable() {
        runOnUiThread(() -> {
            hideLoading();
            mTvLoginError.setVisibility(GONE);
            mPbLoginLoading.setVisibility(View.INVISIBLE);
            Drawable drawable = VectorDrawableCompat.create(getResources(), R.drawable.ic_check, null);
            mTvLoginAvailability.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
            mTvLoginAvailability.setTextColor(ContextCompat.getColor(this, R.color.loginSuccess));
            mTvLoginAvailability.setText(getString(R.string.login_error_login_available));
            mTvLoginAvailability.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void showLoginUnavailable() {
        runOnUiThread(() -> {
            hideLoading();
            mPbLoginLoading.setVisibility(View.INVISIBLE);
            Drawable drawable = VectorDrawableCompat.create(getResources(), R.drawable.ic_sad, null);
            mTvLoginAvailability.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
            mTvLoginAvailability.setTextColor(ContextCompat.getColor(this, R.color.loginError));
            mTvLoginAvailability.setText(getString(R.string.login_error_login_not_available));
            mTvLoginAvailability.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void showIncorrectLogin(int message) {
        runOnUiThread(() -> {
            hideLoading();
            mPbLoginLoading.setVisibility(View.INVISIBLE);
            mTvLoginAvailability.setVisibility(View.INVISIBLE);
            mTvLoginError.setText(getString(message));
            mTvLoginError.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void showIncorrectEmail(int message) {
        hideLoading();
        mTvEmailError.setText(getString(message));
        mTvEmailError.setVisibility(View.VISIBLE);
    }

    @Override
    public void showIncorrectPassword(int message) {
        hideLoading();
        mTvNewPasswordError.setText(getString(message));
        mTvNewPasswordError.setVisibility(View.VISIBLE);
    }

    @Override
    public void showIncorrectOldPassword(int message) {
        hideLoading();
        mTvOldPasswordError.setText(getString(message));
        mTvOldPasswordError.setVisibility(View.VISIBLE);
    }

    @Override
    public void showIncorrectConfirmPassword(int message) {
        hideLoading();
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
        showLoadingDialog();
    }

    @Override
    public void showContent() {
        // Stub
    }

    @Override
    public void hideAll() {
        hideLoading();
        mTvConfirmPasswordError.setVisibility(GONE);
        mTvEmailError.setVisibility(GONE);
        mTvLoginError.setVisibility(GONE);
        mTvNewPasswordError.setVisibility(GONE);
        mTvOldPasswordError.setVisibility(GONE);
        mTvLoginAvailability.setVisibility(View.INVISIBLE);
        mPbLoginLoading.setVisibility(View.INVISIBLE);
    }

    private void hideLoading() {
        hideLoadingDialog();
    }
}
