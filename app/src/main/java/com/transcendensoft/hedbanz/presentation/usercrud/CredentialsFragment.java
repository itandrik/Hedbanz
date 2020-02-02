package com.transcendensoft.hedbanz.presentation.usercrud;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.presentation.base.BaseFragment;
import com.transcendensoft.hedbanz.presentation.mainscreen.MainActivity;
import com.transcendensoft.hedbanz.utils.AndroidUtils;
import com.transcendensoft.hedbanz.utils.KeyboardUtils;
import com.transcendensoft.hedbanz.utils.ViewUtils;
import com.transcendensoft.hedbanz.utils.extension.FragmentExtensionsKt;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
import static com.google.android.gms.internal.zzahf.runOnUiThread;

public class CredentialsFragment extends BaseFragment implements UserCrudContract.View {
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_credentials, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int size = ViewUtils.dpToPx(requireContext(), 100);
        Glide.with(this).asGif().load(R.raw.smile_gif_new).preload(size, size);

        initPasswordIcon();
        initUserData();
        initToolbar();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.bindView(this);
            mPresenter.initNameCheckingListener(mEtLogin);
            initEditTextListeners();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.unbindView();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.destroy();
        }
    }

    /*------------------------------------*
     *---------- Initialization ----------*
     *------------------------------------*/

    private void initToolbar() {
        FragmentExtensionsKt.setupNavigationToolbar(this,
                ((MainActivity) requireActivity()).getToolbar(),
                getString(R.string.credentials_title)
        );
    }

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
        DrawableCompat.setTint(drawable, ContextCompat.getColor(requireContext(), R.color.textDarkRed));
        mEtOldPassword.setCompoundDrawablesWithIntrinsicBounds(drawable, null,null,null);
        mEtNewPassword.setCompoundDrawablesWithIntrinsicBounds(drawable, null,null,null);
    }

    /*------------------------------------*
     *--------- Set data to view ---------*
     *------------------------------------*/
    @Override
    public void crudOperationSuccess() {
        hideLoadingDialog();
        mEtNewPassword.setText("");
        mEtConfirmPassword.setText("");
        mEtOldPassword.setText("");
        AndroidUtils.showShortToast(requireContext(), R.string.credentials_update_success);
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
            mTvLoginAvailability.setTextColor(ContextCompat.getColor(requireContext(), R.color.loginSuccess));
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
            mTvLoginAvailability.setTextColor(ContextCompat.getColor(requireContext(), R.color.loginError));
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
        AndroidUtils.showShortToast(requireContext(), R.string.error_server);
    }

    @Override
    public void showNetworkError() {
        hideAll();
        AndroidUtils.showShortToast(requireContext(), R.string.error_network);
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
