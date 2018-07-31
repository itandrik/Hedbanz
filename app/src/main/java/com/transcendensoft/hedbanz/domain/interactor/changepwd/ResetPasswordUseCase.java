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
import com.transcendensoft.hedbanz.data.repository.UserDataRepositoryImpl;
import com.transcendensoft.hedbanz.domain.CompletableUseCase;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.domain.interactor.changepwd.exception.PasswordResetException;
import com.transcendensoft.hedbanz.domain.repository.UserDataRepository;
import com.transcendensoft.hedbanz.domain.validation.PasswordResetError;
import com.transcendensoft.hedbanz.domain.validation.UserCrudValidator;
import com.transcendensoft.hedbanz.domain.validation.UserError;
import com.transcendensoft.hedbanz.utils.SecurityUtils;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableTransformer;
import io.reactivex.disposables.CompositeDisposable;

/**
 * This class is an implementation of {@link com.transcendensoft.hedbanz.domain.UseCase}
 * that represents a use case for user reseting password.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class ResetPasswordUseCase extends CompletableUseCase<ResetPasswordUseCase.Param>{
    private UserDataRepository mUserDataRepository;
    private PasswordResetException mException;

    @Inject
    public ResetPasswordUseCase(CompletableTransformer completableTransformer,
                                CompositeDisposable mCompositeDisposable,
                                UserDataRepositoryImpl userDataRepository) {
        super(completableTransformer, mCompositeDisposable);
        this.mUserDataRepository = userDataRepository;
    }

    @Override
    protected Completable buildUseCaseCompletable(ResetPasswordUseCase.Param param) {
        mException = new PasswordResetException();
        if (isDataCorrect(param)) {
            param.setPassword(SecurityUtils.hash(param.getPassword()));

            return mUserDataRepository.resetPassword(param.login, param.keyword, param.password)
                    .onErrorResumeNext(this::processOnError);
        }
        return Completable.error(mException);
    }

    private boolean isDataCorrect(ResetPasswordUseCase.Param param){
        User user = new User.Builder()
                .setPassword(param.password)
                .setConfirmPassword(param.confirmPassword)
                .build();
        UserCrudValidator validator = new UserCrudValidator(user);
        boolean result = true;
        if (!validator.isPasswordValid()) {
            if (validator.getError() == UserError.EMPTY_PASSWORD) {
                mException.addError(PasswordResetError.EMPTY_PASSWORD);
            } else if (validator.getError() == UserError.INVALID_PASSWORD) {
                mException.addError(PasswordResetError.INCORRECT_PASSWORD);
            }
            result = false;
        }
        if (!validator.isConfirmPasswordValid()) {
            if (validator.getError() == UserError.EMPTY_PASSWORD_CONFIRMATION) {
                mException.addError(PasswordResetError.EMPTY_PASSWORD_CONFIRMATION);
            } else if (validator.getError() == UserError.INVALID_PASSWORD_CONFIRMATION) {
                mException.addError(PasswordResetError.INCORRECT_PASSWORD_CONFIRMATION);
            }
            result = false;
        }
        return result;
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

    public static class Param{
        private String login;
        private String keyword;
        private String password;
        private String confirmPassword;

        public Param(String login, String keyword, String password, String confirmPassword) {
            this.login = login;
            this.keyword = keyword;
            this.password = password;
            this.confirmPassword = confirmPassword;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getConfirmPassword() {
            return confirmPassword;
        }

        public void setConfirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
        }
    }
}
