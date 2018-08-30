package com.transcendensoft.hedbanz.domain.interactor.user;
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
import com.transcendensoft.hedbanz.data.repository.UserDataRepositoryImpl;
import com.transcendensoft.hedbanz.data.source.DataPolicy;
import com.transcendensoft.hedbanz.domain.ObservableUseCase;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.domain.interactor.user.exception.UserCredentialsException;
import com.transcendensoft.hedbanz.domain.repository.UserDataRepository;
import com.transcendensoft.hedbanz.domain.validation.UserCrudValidator;
import com.transcendensoft.hedbanz.domain.validation.UserError;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;

/**
 * This class is an implementation of {@link com.transcendensoft.hedbanz.domain.UseCase}
 * that represents a use case for user credentials updating. Data related to a specific
 * {@link com.transcendensoft.hedbanz.domain.entity.User}.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */

public class UpdateUserInteractor extends ObservableUseCase<User, UpdateUserInteractor.Params> {
    private UserCredentialsException mUserException;
    private UserDataRepository mUserRepository;
    private PreferenceManager mPreferenceManager;

    @Inject
    public UpdateUserInteractor(ObservableTransformer mSchedulersTransformer,
                                CompositeDisposable mCompositeDisposable,
                                UserDataRepositoryImpl userRepository,
                                PreferenceManager preferenceManager) {
        super(mSchedulersTransformer, mCompositeDisposable);
        mUserRepository = userRepository;
        mPreferenceManager = preferenceManager;
    }

    @Override
    protected Observable<User> buildUseCaseObservable(UpdateUserInteractor.Params params) {
        mUserException = new UserCredentialsException();

        User user = params.getUser();
        String oldPassword = params.getOldPassword();
        user.setId(mPreferenceManager.getUser().getId());

        if (isUserValid(user) & isOldPasswordValid(oldPassword)) { // needs to check both states
            //user.setPassword(SecurityUtils.hash(user.getPassword()));
            //oldPassword = SecurityUtils.hash(oldPassword);

            return mUserRepository.updateUser(user.getId(), user.getLogin(),
                    oldPassword, user.getPassword(), DataPolicy.API)
                    .onErrorResumeNext(this::processUpdateUserOnError);
        }
        return Observable.error(mUserException);
    }

    private boolean isUserValid(com.transcendensoft.hedbanz.domain.entity.User user) {
        UserCrudValidator validator = new UserCrudValidator(user);
        boolean result = true;
        if (!validator.isLoginValid()) {
            mUserException.addUserError(validator.getError());
            result = false;
        }
        if (!validator.isEmailValid()) {
            mUserException.addUserError(validator.getError());
            result = false;
        }
        if (!validator.isPasswordValid()) {
            mUserException.addUserError(validator.getError());
            result = false;
        }
        if (!validator.isConfirmPasswordValid()) {
            mUserException.addUserError(validator.getError());

            result = false;
        }
        return result;
    }

    private boolean isOldPasswordValid(String oldPassword) {
        UserCrudValidator validator = new UserCrudValidator(new User.Builder().build());
        if (!validator.isOldPasswordValid(oldPassword)) {
            mUserException.addUserError(validator.getError());
            return false;
        } else {
            return true;
        }
    }

    private Observable<User> processUpdateUserOnError(Throwable throwable) {
        if (throwable instanceof HedbanzApiException) {
            HedbanzApiException exception = (HedbanzApiException) throwable;
            mUserException.addUserError(
                    UserError.getUserErrorByCode(
                            exception.getServerErrorCode()));
        } else {
            mUserException.addUserError(UserError.UNDEFINED_ERROR);
        }
        return Observable.error(mUserException);
    }

    public static final class Params {
        private User user;
        private String oldPassword;

        public Params() {
        }

        public User getUser() {
            return user;
        }

        public Params setUser(User user) {
            this.user = user;
            return this;
        }

        public String getOldPassword() {
            return oldPassword;
        }

        public Params setOldPassword(String oldPassword) {
            this.oldPassword = oldPassword;
            return this;
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        mUserRepository.disconnectIsLoginAvailable();
    }
}