package com.transcendensoft.hedbanz.view.fragment;

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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.model.entity.User;
import com.transcendensoft.hedbanz.presenter.PresenterManager;
import com.transcendensoft.hedbanz.presenter.impl.LoginPresenterImpl;
import com.transcendensoft.hedbanz.util.AndroidUtils;
import com.transcendensoft.hedbanz.view.LoginView;
import com.transcendensoft.hedbanz.view.activity.MainActivity;
import com.transcendensoft.hedbanz.view.activity.RegisterActivity;

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
public class LoginFragment extends Fragment implements LoginView{
    @BindView(R.id.etLogin) EditText mEtLogin;
    @BindView(R.id.etPassword) EditText mEtPassword;
    @BindView(R.id.tvErrorLogin) TextView mTvLoginError;
    @BindView(R.id.tvErrorPassword) TextView mTvPasswordError;

    private ProgressDialog mProgressDialog;
    private LoginPresenterImpl mPresenter;

    /*------------------------------------*
     *-------- Activity lifecycle --------*
     *------------------------------------*/
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        ButterKnife.bind(this, view);

        initProgressDialog();
        initPresenter(savedInstanceState);

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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        PresenterManager.getInstance().savePresenter(mPresenter, outState);
    }

    @Override
    public Context provideContext() {
        return getActivity();
    }

    /*------------------------------------*
     *---------- Initialization ----------*
     *------------------------------------*/
    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(getString(R.string.action_loading));
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
    }

    private void initPresenter(Bundle savedInstanceState){
        if (savedInstanceState == null) {
            mPresenter = new LoginPresenterImpl();
        } else {
            mPresenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }
    }

    /*------------------------------------*
     *-------- On click listeners --------*
     *------------------------------------*/
    @OnClick(R.id.btnRegister)
    protected void onRegisterClicked() {
        hideAll();
        Intent intent = new Intent(getActivity(), RegisterActivity.class);
        startActivity(intent);
        if(getActivity() != null) {
            getActivity().overridePendingTransition(R.anim.login_page_right_in, R.anim.login_page_right_out);
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
        hideLoading();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /*------------------------------------*
         *-------- Error and loading ---------*
         *------------------------------------*/
    @Override
    public void showServerError() {
        hideLoading();
        AndroidUtils.showShortToast(getActivity(), R.string.error_server);
    }

    @Override
    public void showNetworkError() {
        hideLoading();
        AndroidUtils.showShortToast(getActivity(), R.string.error_network);
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

    @Override
    public void showLoginError(int message) {
        hideLoading();
        mTvLoginError.setVisibility(View.VISIBLE);
        mTvLoginError.setText(getString(message));
    }

    @Override
    public void showPasswordError(int message) {
        hideLoading();
        mTvPasswordError.setVisibility(View.VISIBLE);
        mTvPasswordError.setText(getString(message));
    }

    private void hideAll(){
        hideLoading();
        mTvLoginError.setVisibility(GONE);
        mTvPasswordError.setVisibility(GONE);
    }

    private void hideLoading(){
        if(mProgressDialog != null){
            mProgressDialog.hide();
        }
    }
}
