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

import com.crashlytics.android.Crashlytics;
import com.transcendensoft.hedbanz.model.api.manager.UserCrudApiManager;
import com.transcendensoft.hedbanz.model.data.PreferenceManager;
import com.transcendensoft.hedbanz.model.entity.ServerResult;
import com.transcendensoft.hedbanz.model.entity.ServerStatus;
import com.transcendensoft.hedbanz.model.entity.User;
import com.transcendensoft.hedbanz.model.entity.error.LoginError;
import com.transcendensoft.hedbanz.model.entity.error.ServerError;
import com.transcendensoft.hedbanz.presenter.BasePresenter;
import com.transcendensoft.hedbanz.presenter.LoginPresenter;
import com.transcendensoft.hedbanz.presenter.validation.UserCrudValidator;
import com.transcendensoft.hedbanz.util.AndroidUtils;
import com.transcendensoft.hedbanz.view.LoginView;

import io.reactivex.disposables.Disposable;

/**
 * Presenter from MVP pattern, that contains
 * methods to process log in work.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */

public class LoginPresenterImpl extends BasePresenter<User, LoginView> implements LoginPresenter {
    private static final String TAG = LoginPresenterImpl.class.getName();

    @Override
    protected void updateView() {

    }

    @Override
    public void login(User user) {
        setModel(user);
        if (isUserValid(user)) {
            Disposable disposable = UserCrudApiManager.getInstance()
                    .authUser(user)
                    .subscribe(
                            this::processRegisterOnNext,
                            this::processRegisterOnError,
                            () -> view().loginSuccess(),
                            this::processRegisterOnSubscribe);
            addDisposable(disposable);
        }
    }

    private boolean isUserValid(User user) {
        UserCrudValidator validator = new UserCrudValidator(user);
        boolean result = true;
        if (!validator.isLoginValid()) {
            view().showLoginError(validator.getErrorMessage());
            result = false;
        }
        if (!validator.isPasswordValid()) {
            view().showPasswordError(validator.getErrorMessage());
            result = false;
        }
        return result;
    }

    private void processRegisterOnNext(ServerResult<User> result) {
        if (result == null) {
            throw new RuntimeException("Server result is null");
        } else if (!result.getStatus().equalsIgnoreCase(ServerStatus.SUCCESS.toString())) {
            ServerError serverError = result.getServerError();
            if (serverError != null) {
                processErrorFromServer(serverError);
            }
            throw new IllegalStateException();
        } else {
            if(result.getData() != null) {
                new PreferenceManager(view().provideContext()).setUser(result.getData());
            } else {
                throw new RuntimeException("User comes NULL from server while login");
            }
        }
    }

    private void processErrorFromServer(ServerError serverError) {
        for (LoginError loginError : LoginError.values()) {
            if (loginError.getErrorCode() == serverError.getErrorCode()) {
                switch (loginError) {
                    case NO_SUCH_USER:
                    case EMPTY_LOGIN:
                        view().showLoginError(loginError.getErrorMessage());
                        break;
                    case INCORRECT_PASSWORD:
                    case EMPTY_PASSWORD:
                        view().showPasswordError(loginError.getErrorMessage());
                        break;
                    default:
                        view().showServerError();
                        break;
                }
            }
        }
    }

    private void processRegisterOnError(Throwable err) {
        if (!(err instanceof IllegalStateException)) {
            Log.e(TAG, "Error " + err.getMessage());
            Crashlytics.logException(err);
            view().showServerError();
        }
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
}
