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

import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.transcendensoft.hedbanz.data.exception.HedbanzApiException;
import com.transcendensoft.hedbanz.data.models.common.ServerError;
import com.transcendensoft.hedbanz.data.models.mapper.UserModelDataMapper;
import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;
import com.transcendensoft.hedbanz.di.scope.ActivityScope;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.domain.repository.UserDataRepository;
import com.transcendensoft.hedbanz.domain.validation.LoginError;
import com.transcendensoft.hedbanz.domain.validation.UserCrudValidator;
import com.transcendensoft.hedbanz.presentation.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * Presenter from MVP pattern, that contains
 * methods to process log in work.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
@ActivityScope
public class LoginPresenter extends BasePresenter<User, LoginContract.View>
        implements LoginContract.Presenter {
    private static final String TAG = LoginPresenter.class.getName();
    private PreferenceManager mPreferenceManager;
    private UserDataRepository mUserRepository;

    @Inject
    public LoginPresenter(PreferenceManager preferenceManager, UserDataRepository userRepository) {
        this.mPreferenceManager = preferenceManager;
        this.mUserRepository = userRepository;
    }

    @Override
    protected void updateView() {

    }

    @Override
    public void login(User user) {
        setModel(user);
        if (isUserValid(user)) {
            Disposable disposable = mUserRepository
                    .authUser(user)
                    .subscribe(
                            this::processLoginOnNext,
                            this::processLoginOnError,
                            () -> view().loginSuccess(),
                            this::processOnSubscribe);
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

    private void processLoginOnNext(User user) {
        /*if (result == null) {
            throw new RuntimeException("Server result is null");
        } else if (!result.getStatus().equalsIgnoreCase(ServerStatus.SUCCESS.toString())) {
            ServerError serverError = result.getServerError();
            if (serverError != null) {
                processErrorFromServer(serverError);
            }
            throw new IllegalStateException();
        } else {*/
            if(user != null) {
                mPreferenceManager.setUser(new UserModelDataMapper().convert(user));
            } else {
                throw new HedbanzApiException("User comes NULL from server while login");
            }
        //}
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

    private void processLoginOnError(Throwable err) {
        if (!(err instanceof IllegalStateException)) {
            Log.e(TAG, "Error " + err.getMessage());
            Crashlytics.logException(err);
            view().showServerError();
        }
    }
}
