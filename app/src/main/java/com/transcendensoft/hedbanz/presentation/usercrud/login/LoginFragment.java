package com.transcendensoft.hedbanz.presentation.usercrud.login;

/**
 * Copyright 2017. Andrii Chernysh
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.presentation.StartActivity;
import com.transcendensoft.hedbanz.presentation.base.BaseFragment;
import com.transcendensoft.hedbanz.presentation.mainscreen.MainActivity;
import com.transcendensoft.hedbanz.presentation.restorepwd.RestorePasswordActivity;
import com.transcendensoft.hedbanz.presentation.usercrud.RegisterActivity;
import com.transcendensoft.hedbanz.utils.ViewUtils;
import com.transcendensoft.hedbanz.utils.extension.ViewExtensionsKt;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
import static com.transcendensoft.hedbanz.domain.entity.HedbanzAnalyticsKt.AUTH_BUTTON;
import static com.transcendensoft.hedbanz.domain.entity.HedbanzAnalyticsKt.PASSWORD_RECOVERY_BUTTON;
import static com.transcendensoft.hedbanz.domain.entity.HedbanzAnalyticsKt.REGISTER_BUTTON;

/**
 * Fragment that show standard form for login and
 * buttons for register and password recovery.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class LoginFragment extends BaseFragment implements LoginContract.View{
    @BindView(R.id.etLogin) EditText mEtLogin;
    @BindView(R.id.etPassword) EditText mEtPassword;
    @BindView(R.id.tvErrorLogin) TextView mTvLoginError;
    @BindView(R.id.tvErrorPassword) TextView mTvPasswordError;
    @BindView(R.id.parent) ScrollView mParentLayout;
    @BindView(R.id.llRegisterContainer) LinearLayout mLlRegister;
    @BindView(R.id.ivLogo) ImageView mIvLogo;

    @Inject LoginPresenter mPresenter;
    @Inject StartActivity mActivity;
    @Inject PreferenceManager mPreferenceManager;
    @Inject FirebaseAnalytics mFirebaseAnalytics;

    @Inject
    public LoginFragment() {
        // Requires empty public constructor
    }

    /*------------------------------------*
     *-------- Activity lifecycle --------*
     *------------------------------------*/
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        ButterKnife.bind(this, view);
        ViewExtensionsKt.setupKeyboardHiding(view, mActivity);
        ViewExtensionsKt.hasNavbar(mActivity);

        initPasswordIcon();
        initNavBar();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.bindView(this);
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
        mPresenter.destroy();
    }

    /*------------------------------------*
     *---------- Initialization ----------*
     *------------------------------------*/

    private void initPasswordIcon(){
        Drawable drawable = VectorDrawableCompat.create(
                getResources(), R.drawable.ic_password, null);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, ContextCompat.getColor(mActivity, R.color.textDarkRed));
        mEtPassword.setCompoundDrawablesWithIntrinsicBounds(drawable, null,null,null);
    }

    private void initNavBar(){
        if(ViewExtensionsKt.hasNavbar(mActivity)){
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mLlRegister.getLayoutParams();
            params.bottomMargin = ViewUtils.dpToPx(mActivity, 24);
            mLlRegister.setLayoutParams(params);
        }
    }

    /*------------------------------------*
     *-------- On click listeners --------*
     *------------------------------------*/
    @OnClick(R.id.btnRegister)
    protected void onRegisterClicked() {
        hideAll();
        Intent intent = new Intent(mActivity, RegisterActivity.class);
        startActivity(intent);
        if(mActivity != null) {
            mActivity.overridePendingTransition(R.anim.login_page_right_in, R.anim.login_page_right_out);
        }
        mFirebaseAnalytics.logEvent(REGISTER_BUTTON, null);
    }

    @OnClick(R.id.tvPasswordRecovery)
    protected void onRecoveryClicked() {
        hideAll();
        Intent intent = new Intent(mActivity, RestorePasswordActivity.class);
        startActivity(intent);
        if(mActivity != null) {
            mActivity.overridePendingTransition(R.anim.login_page_left_in, R.anim.login_page_left_out);
        }
        mFirebaseAnalytics.logEvent(PASSWORD_RECOVERY_BUTTON, null);
    }

    @OnClick(R.id.btnEnter)
    protected void onLoginClicked(){
        hideAll();
        if(mPresenter != null){
            User user = new User.Builder()
                    .setLogin(mEtLogin.getText().toString())
                    .setPassword(mEtPassword.getText().toString())
                    .build();
            mPresenter.login(user);
            mFirebaseAnalytics.logEvent(AUTH_BUTTON, null);
        }
    }

    @Override
    public void loginSuccess() {
        hideLoadingDialog();
        mPreferenceManager.setIsAuthorised(true);
        Intent intent = new Intent(mActivity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /*------------------------------------*
     *-------- Error and loading ---------*
     *------------------------------------*/
    @Override
    public void showServerError() {
        hideLoadingDialog();
        showShortToastMessage(R.string.error_server);
    }

    @Override
    public void showNetworkError() {
        hideLoadingDialog();
        showShortToastMessage(R.string.error_network);
    }

    @Override
    public void showLoading() {
        // Stub
    }

    @Override
    public void showContent() {
        // Stub
    }

    @Override
    public void showLoginError(int message) {
        hideLoadingDialog();
        mTvLoginError.setVisibility(View.VISIBLE);
        mTvLoginError.setText(getString(message));
    }

    @Override
    public void showPasswordError(int message) {
        hideLoadingDialog();
        mTvPasswordError.setVisibility(View.VISIBLE);
        mTvPasswordError.setText(getString(message));
    }

    @Override
    public void hideAll(){
        hideLoadingDialog();
        mTvLoginError.setVisibility(GONE);
        mTvPasswordError.setVisibility(GONE);
    }

    public void showIcon(){
        mIvLogo.animate()
                .alpha(1.f)
                .setDuration(100)
                .setStartDelay(300)
                .start();
    }
}
