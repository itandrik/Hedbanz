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
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.presentation.StartActivity;
import com.transcendensoft.hedbanz.presentation.base.BaseFragment;
import com.transcendensoft.hedbanz.presentation.mainscreen.MainActivity;
import com.transcendensoft.hedbanz.presentation.usercrud.RegisterActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;

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

    @Inject LoginPresenter mPresenter;
    @Inject StartActivity mActivity;
    @Inject PreferenceManager mPreferenceManager;

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
        initPasswordIcon();

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

    /*------------------------------------*
     *---------- Initialization ----------*
     *------------------------------------*/

    private void initPasswordIcon(){
        Drawable drawable = VectorDrawableCompat.create(
                getResources(), R.drawable.ic_password, null);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, ContextCompat.getColor(getActivity(), R.color.textDarkRed));
        mEtPassword.setCompoundDrawablesWithIntrinsicBounds(drawable, null,null,null);
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
}
