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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;

import com.transcendensoft.hedbanz.di.FragmentModule;
import com.transcendensoft.hedbanz.di.component.FragmentComponent;
import com.transcendensoft.hedbanz.di.component.HasComponent;
import com.transcendensoft.hedbanz.di.qualifier.ActivityContext;
import com.transcendensoft.hedbanz.utils.AndroidUtils;
import com.transcendensoft.hedbanz.utils.KeyboardUtils;
import com.transcendensoft.hedbanz.utils.NetworkUtils;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * It is base fragment for all fragments in application.
 * It describes basic operations like show error or loading
 * for all fragments.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public abstract class BaseFragment extends Fragment implements BaseView {
    private static final String TAG = BaseFragment.class.getName();

    @Inject ProgressDialog mProgressDialog;
    @Inject @ActivityContext Context mActivityContext;

    private FragmentComponent mFragmentComponent;
    private Activity mActivity;

    public FragmentComponent getFragmentComponent() {
        return mFragmentComponent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HasComponent) {
            mActivity = (Activity) context;
            mFragmentComponent = ((HasComponent)context).getActivityComponent()
                    .fragmentComponentBuilder()
                    .fragmentModule(new FragmentModule())
                    .build();
            injectDependencies();
        }
    }

    protected abstract void injectDependencies();

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
        View rootView = getView();
        if (rootView != null) {
            Snackbar.make(rootView, getString(messageRes), Snackbar.LENGTH_LONG).show();
        } else {
            Timber.tag(TAG).e("Cant show snack message, because fragment root view is null");
        }
    }

    @Override
    public void showSnackMessage(String message) {
        View rootView = getView();
        if (rootView != null) {
            Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show();
        } else {
            Timber.tag(TAG).e("Cant show snack message, because fragment root view is null");
        }
    }

    @Override
    public void showShortToastMessage(int messageRes) {
        AndroidUtils.showShortToast(mActivityContext, messageRes);
    }

    @Override
    public void showShortToastMessage(String message) {
        AndroidUtils.showShortToast(mActivityContext, message);
    }

    @Override
    public void showLongToastMessage(int messageRes) {
        AndroidUtils.showLongToast(mActivityContext, messageRes);
    }

    @Override
    public void showLongToastMessage(String message) {
        AndroidUtils.showLongToast(mActivityContext, message);
    }

    @Override
    public boolean isNetworkConnected() {
        return NetworkUtils.isNetworkConnected(mActivityContext);
    }

    @Override
    public void handleKeyboard(KeyboardUtils.KeyboardState state, @Nullable EditText editText) {
        switch (state) {
            case HIDE:
                KeyboardUtils.hideSoftInput(mActivity);
                break;
            case SHOW:
                KeyboardUtils.showSoftInput(editText, mActivityContext);
                break;
            case TOGGLE:
                KeyboardUtils.toggleSoftInput(mActivityContext);
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
