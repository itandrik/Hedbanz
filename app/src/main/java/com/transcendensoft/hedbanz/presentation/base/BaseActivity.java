package com.transcendensoft.hedbanz.presentation.base;
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
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;

import com.transcendensoft.hedbanz.utils.AndroidUtils;
import com.transcendensoft.hedbanz.utils.KeyboardUtils;
import com.transcendensoft.hedbanz.utils.NetworkUtils;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

/**
 * It is base activity for all activities in application.
 * It describes basic operations like show error or loading
 * for all activities.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public abstract class BaseActivity extends DaggerAppCompatActivity implements BaseView {
    @Inject ProgressDialog mProgressDialog;
   /* private ActivityComponent mActivityComponent;

    public ActivityComponent getActivityComponent() {
        return mActivityComponent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityComponent = HedbanzApplication.get(this).getApplicationComponent()
                .activityComponentBuilder()
                .activityModule(new ActivityModule(this))
                .baseViewModule(new BaseViewModule())
                .build();
    }*/

    @Override
    public void showSnackError(int messageRes) {
        //TODO
    }

    @Override
    public void showSnackError(String message) {
        //TODO
    }

    @Override
    public void showSnackMessage(int messageRes) {
        View rootView = findViewById(android.R.id.content);
        Snackbar.make(rootView, getString(messageRes), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showSnackMessage(String message) {
        View rootView = findViewById(android.R.id.content);
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showShortToastMessage(int messageRes) {
        AndroidUtils.showShortToast(this, messageRes);
    }

    @Override
    public void showShortToastMessage(String message) {
        AndroidUtils.showShortToast(this, message);
    }

    @Override
    public void showLongToastMessage(int messageRes) {
        AndroidUtils.showLongToast(this, messageRes);
    }

    @Override
    public void showLongToastMessage(String message) {
        AndroidUtils.showLongToast(this, message);
    }

    @Override
    public boolean isNetworkConnected() {
        return NetworkUtils.isNetworkConnected(this);
    }

    @Override
    public void handleKeyboard(KeyboardUtils.KeyboardState state, @Nullable EditText editText) {
        switch (state) {
            case HIDE:
                KeyboardUtils.hideSoftInput(this);
                break;
            case SHOW:
                KeyboardUtils.showSoftInput(editText, this);
                break;
            case TOGGLE:
                KeyboardUtils.toggleSoftInput(this);
                break;
        }
    }

    @Override
    public void showLoadingDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.show();
        }
    }

    @Override
    public void hideLoadingDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.hide();
        }
    }
}
