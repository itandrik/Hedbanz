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

import com.transcendensoft.hedbanz.data.exception.HedbanzApiException;
import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;
import com.transcendensoft.hedbanz.di.scope.ActivityScope;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.domain.interactor.user.AuthorizeUserInteractor;
import com.transcendensoft.hedbanz.domain.interactor.user.exception.UserCredentialsException;
import com.transcendensoft.hedbanz.domain.validation.UserError;
import com.transcendensoft.hedbanz.presentation.base.BasePresenter;

import javax.inject.Inject;

import timber.log.Timber;

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
    private PreferenceManager mPreferenceManager;
    private AuthorizeUserInteractor mAuthorizeUserInteractorInteractor;

    @Inject
    public LoginPresenter(PreferenceManager preferenceManager, AuthorizeUserInteractor authorizeUserInteractorInteractor) {
        this.mPreferenceManager = preferenceManager;
        this.mAuthorizeUserInteractorInteractor = authorizeUserInteractorInteractor;
    }

    @Override
    protected void updateView() {
        // Stub
    }

    @Override
    public void destroy() {
        mAuthorizeUserInteractorInteractor.dispose();
    }

    @Override
    public void login(User user) {
        setModel(user);
        view().showLoadingDialog();
        mAuthorizeUserInteractorInteractor.execute(user,
                this::processLoginOnNext,
                this::processLoginOnError,
                () -> view().loginSuccess());
    }

    private void processLoginOnNext(User user) {
        if (user != null) {
            Timber.tag(LoginPresenter.class.getName()).i("Current user = %s", user.toString());
            mPreferenceManager.setUser(user);
        } else {
            throw new HedbanzApiException("User comes NULL from server while login");
        }
    }

    private void processLoginOnError(Throwable err) {
        if (err instanceof UserCredentialsException) {
            UserCredentialsException exception = (UserCredentialsException) err;
            for (UserError userError: exception.getUserErrors()) {
                processUserError(userError);
            }
        } else {
            Timber.e(err);
            view().showServerError();
        }
    }

    private void processUserError(UserError userError){
        switch (userError) {
            case INVALID_LOGIN:
            case NO_SUCH_USER:
            case EMPTY_LOGIN:
                view().showLoginError(userError.getErrorMessage());
                break;
            case INVALID_PASSWORD:
            case INCORRECT_PASSWORD:
            case EMPTY_PASSWORD:
            case INCORRECT_CREDENTIALS:
                view().showPasswordError(userError.getErrorMessage());
                break;
            default:
                view().showServerError();
                break;
        }
    }
}
