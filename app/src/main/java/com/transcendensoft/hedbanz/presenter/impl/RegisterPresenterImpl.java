package com.transcendensoft.hedbanz.presenter.impl;
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

import android.util.Log;
import android.widget.EditText;

import com.crashlytics.android.Crashlytics;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.transcendensoft.hedbanz.model.api.manager.LoginRegisterManager;
import com.transcendensoft.hedbanz.model.entity.ServerResult;
import com.transcendensoft.hedbanz.model.entity.ServerStatus;
import com.transcendensoft.hedbanz.model.entity.User;
import com.transcendensoft.hedbanz.presenter.BasePresenter;
import com.transcendensoft.hedbanz.presenter.RegisterPresenter;
import com.transcendensoft.hedbanz.presenter.validation.RegisterValidator;
import com.transcendensoft.hedbanz.util.AndroidUtils;
import com.transcendensoft.hedbanz.view.RegisterView;

import io.reactivex.disposables.Disposable;

/**
 * Presenter from MVP pattern, that contains
 * methods to process register work with repository
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class RegisterPresenterImpl extends BasePresenter<User, RegisterView>
        implements RegisterPresenter {
    public static final String TAG = RegisterPresenterImpl.class.getName();

    @Override
    protected void updateView() {

    }

    @Override
    public void registerUser(User user) {
        if (isUserValid(user)) {
            Disposable disposable = LoginRegisterManager.getInstance()
                    .registerUser(user)
                    .subscribe(
                            this::processRegisterOnNext,
                            this::processRegisterOnError,
                            () -> view().registerSuccess(),
                            this::processRegisterOnSubscribe);
            addDisposable(disposable);
        }
    }

    private boolean isUserValid(User user) {
        RegisterValidator validator = new RegisterValidator(user);
        boolean result = true;
        if (!validator.isLoginValid()) {
            view().showIncorrectLogin(validator.getErrorMessage());
            result = false;
        }
        if (!validator.isEmailValid()) {
            view().showIncorrectEmail(validator.getErrorMessage());
            result = false;
        }
        if (!validator.isPasswordValid()) {
            view().showIncorrectPassword(validator.getErrorMessage());
            result = false;
        }
        if (!validator.isConfirmPasswordValid()) {
            view().showIncorrectConfirmPassword(validator.getErrorMessage());
            result = false;
        }
        return result;
    }

    private void processRegisterOnNext(ServerResult<User> result) {
        if (result == null) {
            throw new RuntimeException("Server result is null");
        } else if (!result.getStatus().equalsIgnoreCase(ServerStatus.SUCCESS.toString())) {
            throw new RuntimeException("from server : result.getErrorMessage()");
        }
    }

    private void processRegisterOnError(Throwable err) {
        Log.e(TAG, "Error " + err.getMessage());
        Crashlytics.logException(err);
        view().showServerError();
    }

    private void processRegisterOnSubscribe(Disposable d) {
        if (!d.isDisposed() && view().provideContext() != null) {
            if (AndroidUtils.isNetworkConnected(view().provideContext())) {
                view().showLoading();
            } else {
                view().showNetworkError();
            }
        }
    }

    @Override
    public void initAnimEditTextListener(EditText editText) {
        addDisposable(
                RxTextView.textChanges(editText)
                        .subscribe(text -> view().startSmileAnimation()));
    }
}
