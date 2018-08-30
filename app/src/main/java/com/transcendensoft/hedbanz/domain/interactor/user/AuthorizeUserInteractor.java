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
import com.transcendensoft.hedbanz.data.repository.UserDataRepositoryImpl;
import com.transcendensoft.hedbanz.domain.ObservableUseCase;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.domain.interactor.user.exception.UserCredentialsException;
import com.transcendensoft.hedbanz.domain.repository.UserDataRepository;
import com.transcendensoft.hedbanz.domain.validation.UserCrudValidator;
import com.transcendensoft.hedbanz.domain.validation.UserError;
import com.transcendensoft.hedbanz.utils.SecurityUtils;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;

/**
 * This class is an implementation of {@link com.transcendensoft.hedbanz.domain.UseCase}
 * that represents a use case for user authorization. Data related to a specific
 * {@link com.transcendensoft.hedbanz.domain.entity.User}.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */

public class AuthorizeUserInteractor extends ObservableUseCase<User, User> {
    private UserCredentialsException mUserException;
    private UserDataRepository mUserRepository;

    @Inject
    public AuthorizeUserInteractor(ObservableTransformer mSchedulersTransformer,
                                   CompositeDisposable mCompositeDisposable,
                                   UserDataRepositoryImpl userRepository) {
        super(mSchedulersTransformer, mCompositeDisposable);
        mUserRepository = userRepository;
    }

    @Override
    protected Observable<User> buildUseCaseObservable(User params) {
        mUserException = new UserCredentialsException();

        if(isUserValid(params)){
            params.setPassword(SecurityUtils.hash(params.getPassword()));
            return mUserRepository.authUser(params)
                    .onErrorResumeNext(this::processAuthUserOnError);
        }
        return Observable.error(mUserException);
    }

    private boolean isUserValid(User user) {
        UserCrudValidator validator = new UserCrudValidator(user);
        boolean result = true;
        if (!validator.isLoginValid()) {
            mUserException.addUserError(validator.getError());
            result = false;
        }
        if (!validator.isPasswordValid()) {
            mUserException.addUserError(validator.getError());
            result = false;
        }
        return result;
    }

    private Observable<User> processAuthUserOnError(Throwable throwable) {
        if(throwable instanceof HedbanzApiException){
            HedbanzApiException exception = (HedbanzApiException) throwable;
            mUserException.addUserError(
                    UserError.getUserErrorByCode(
                            exception.getServerErrorCode()));
        } else {
            mUserException.addUserError(UserError.UNDEFINED_ERROR);
        }

        return Observable.error(mUserException);
    }

    @Override
    public void dispose() {
        super.dispose();
        mUserRepository.disconnectIsLoginAvailable();
    }
}
