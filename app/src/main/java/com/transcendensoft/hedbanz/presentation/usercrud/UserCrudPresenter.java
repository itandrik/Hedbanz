package com.transcendensoft.hedbanz.presentation.usercrud;
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

import android.text.TextUtils;
import android.widget.EditText;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.transcendensoft.hedbanz.data.exception.HedbanzApiException;
import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;
import com.transcendensoft.hedbanz.di.scope.ActivityScope;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.domain.interactor.user.IsLoginAvailableInteractor;
import com.transcendensoft.hedbanz.domain.interactor.user.RegisterUserInteractor;
import com.transcendensoft.hedbanz.domain.interactor.user.UpdateUserInteractor;
import com.transcendensoft.hedbanz.domain.interactor.user.exception.UserCredentialsException;
import com.transcendensoft.hedbanz.domain.validation.UserCrudValidator;
import com.transcendensoft.hedbanz.domain.validation.UserError;
import com.transcendensoft.hedbanz.presentation.base.BasePresenter;

import java.net.ConnectException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Presenter from MVP pattern, that contains
 * methods to process register work with repository
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
@ActivityScope
public class UserCrudPresenter extends BasePresenter<User, UserCrudContract.View>
        implements UserCrudContract.Presenter {
    private IsLoginAvailableInteractor mIsLoginAvailableInteractor;
    private RegisterUserInteractor mRegisterUserInteractorInteractor;
    private UpdateUserInteractor mUpdateUserInteractorInteractor;
    private PreferenceManager mPreferenceManager;

    /*------------------------------------*
     *---------- Initialization ----------*
     *------------------------------------*/
    @Inject
    public UserCrudPresenter(RegisterUserInteractor registerUserInteractorInteractor,
                             UpdateUserInteractor updateUserInteractorInteractor,
                             IsLoginAvailableInteractor isLoginAvailableInteractor,
                             PreferenceManager preferenceManager) {
        this.mRegisterUserInteractorInteractor = registerUserInteractorInteractor;
        this.mUpdateUserInteractorInteractor = updateUserInteractorInteractor;
        this.mPreferenceManager = preferenceManager;
        this.mIsLoginAvailableInteractor = isLoginAvailableInteractor;
    }

    @Override
    protected void updateView() {
        // Stub
    }

    @Override
    public void destroy() {
        mRegisterUserInteractorInteractor.dispose();
        mUpdateUserInteractorInteractor.dispose();
        mIsLoginAvailableInteractor.dispose();
    }

    /*------------------------------------*
     *--------- Crud operations ----------*
     *------------------------------------*/
    @Override
    public void registerUser(User user) {
        setModel(user);
        view().showLoadingDialog();
        mRegisterUserInteractorInteractor.execute(user,
                this::processRegisterOnNext,
                this::processRegisterOnError,
                () -> view().crudOperationSuccess());
    }

    @Override
    public void updateUser(User user, String oldPassword) {
        setModel(user);
        UpdateUserInteractor.Params params = new UpdateUserInteractor.Params()
                .setUser(user)
                .setOldPassword(oldPassword);

        view().showLoadingDialog();
        mUpdateUserInteractorInteractor.execute(params,
                this::processRegisterOnNext,
                this::processRegisterOnError,
                () -> view().crudOperationSuccess());
    }

    private void processRegisterOnNext(User user) {
        if (user != null) {
            Timber.tag(UserCrudPresenter.class.getName()).i("Current user = %s", user.toString());
            mPreferenceManager.setUser(user);
        } else {
            throw new HedbanzApiException("User comes NULL from server while login");
        }
    }

    private void processRegisterOnError(Throwable err) {
        if ((err instanceof UserCredentialsException)) {
            UserCredentialsException exception = (UserCredentialsException) err;
            for (UserError userError : exception.getUserErrors()) {
                processUserError(userError);
            }
        } else {
            Timber.e(err);
            view().showServerError();
        }
    }

    private void processUserError(UserError userError) {
        switch (userError) {
            case INVALID_EMAIL:
            case SUCH_EMAIL_ALREADY_USING:
            case EMPTY_EMAIL:
                view().showIncorrectEmail(userError.getErrorMessage());
                break;
            case SUCH_LOGIN_ALREADY_EXIST:
                view().showLoginUnavailable();
                break;
            case EMPTY_LOGIN:
            case INVALID_LOGIN:
                view().showIncorrectLogin(userError.getErrorMessage());
                break;
            case EMPTY_PASSWORD:
            case INVALID_PASSWORD:
                view().showIncorrectPassword(userError.getErrorMessage());
                break;
            case INVALID_OLD_PASSWORD:
                view().showIncorrectOldPassword(userError.getErrorMessage());
                break;
            case INVALID_PASSWORD_CONFIRMATION:
                view().showIncorrectConfirmPassword(userError.getErrorMessage());
                break;
            default:
                view().showServerError();
                break;
        }
    }

    /*------------------------------------*
     *------- Animation for smile --------*
     *------------------------------------*/
    @Override
    public void initAnimEditTextListener(EditText editText) {
        RxTextView.textChanges(editText)
                .skip(1)
                .doOnEach(text -> {
                    view().startSmileAnimation();
                })
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe(
                        text -> {
                            view().stopSmileAnimation();
                        },
                        err -> {
                            Timber.e("Error while setting start/stop smile animation. " +
                                    "Message : " + err.getMessage());
                        });
    }

    /*------------------------------------*
     *- Checking for login availability --*
     *------------------------------------*/
    @Override
    public void initNameCheckingListener(EditText editText) {
        RxTextView.textChanges(editText).debounce(400, TimeUnit.MILLISECONDS)
                .filter(this::isCorrectLoginInput)
                .subscribe(text -> {
                    view().showLoginAvailabilityLoading();
                    mIsLoginAvailableInteractor.execute(
                            text.toString(),
                            this::processLoginAvailability,
                            this::processLoginAvailabilityError);
                });
    }

    private void processLoginAvailability(Boolean isLoginAvailable) {
        if(view() != null) {
            if (isLoginAvailable) {
                view().showLoginAvailable();
            } else {
                view().showLoginUnavailable();
            }
        }
    }

    private void processLoginAvailabilityError(Throwable err) {
        if(view() != null) {
            if (err instanceof ConnectException) {
                view().showNetworkError();
            } else {
                view().showServerError();
            }
        }
    }

    private boolean isCorrectLoginInput(CharSequence text) {
        if (text != null && !TextUtils.isEmpty(text)) {
            User user = new User.Builder().setLogin(text.toString()).build();
            UserCrudValidator validator = new UserCrudValidator(user);
            if (!validator.isLoginValid()) {
                view().showIncorrectLogin(validator.getError().getErrorMessage());
                return false;
            }
            return true;
        } else {
            view().hideLoginAvailability();
            return false;
        }
    }
}
