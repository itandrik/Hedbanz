package com.transcendensoft.hedbanz.domain.interactor.changepwd;
/**
 * Copyright 2018. Andrii Chernysh
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
import com.transcendensoft.hedbanz.domain.CompletableUseCase;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.domain.interactor.changepwd.exception.PasswordResetException;
import com.transcendensoft.hedbanz.domain.repository.UserDataRepository;
import com.transcendensoft.hedbanz.domain.validation.PasswordResetError;
import com.transcendensoft.hedbanz.domain.validation.UserCrudValidator;
import com.transcendensoft.hedbanz.domain.validation.UserError;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableTransformer;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

/**
 * This class is an implementation of {@link com.transcendensoft.hedbanz.domain.UseCase}
 * that represents a use case for sending email to
 * user that forgot his password
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class ForgotPasswordUseCase extends CompletableUseCase<String> {
    private UserDataRepository mUserDataRepository;
    private PasswordResetException mException;
    private PreferenceManager mPreferenceManager;

    @Inject
    public ForgotPasswordUseCase(CompletableTransformer completableTransformer,
                                 CompositeDisposable mCompositeDisposable,
                                 UserDataRepositoryImpl userDataRepository,
                                 PreferenceManager preferenceManager) {
        super(completableTransformer, mCompositeDisposable);
        this.mUserDataRepository = userDataRepository;
        this.mPreferenceManager = preferenceManager;
    }

    @Override
    protected Completable buildUseCaseCompletable(String login) {
        mException = new PasswordResetException();
        if (isLoginCorrect(login)) {
            String locale = mPreferenceManager.getLocale();
            Timber.i("LOCALE: " + locale);
            return mUserDataRepository.forgotPassword(login, locale)
                    .onErrorResumeNext(this::processOnError);
        }
        return Completable.error(mException);
    }

    private boolean isLoginCorrect(String login) {
        User user = new User.Builder()
                .setLogin(login)
                .build();
        UserCrudValidator validator = new UserCrudValidator(user);
        if (!validator.isLoginValid()) {
            if (validator.getError() == UserError.EMPTY_LOGIN) {
                mException.addError(PasswordResetError.EMPTY_LOGIN);
            } else if (validator.getError() == UserError.INVALID_LOGIN) {
                mException.addError(PasswordResetError.INCORRECT_LOGIN);
            }
            return false;
        }
        return true;
    }

    private Completable processOnError(Throwable throwable) {
        if(throwable instanceof HedbanzApiException){
            HedbanzApiException exception = (HedbanzApiException) throwable;
            mException.addError(
                    PasswordResetError.Companion.getErrorByCode(
                            exception.getServerErrorCode()));
        } else {
            mException.addError(PasswordResetError.UNDEFINED_ERROR);
        }

        return Completable.error(mException);
    }
}
