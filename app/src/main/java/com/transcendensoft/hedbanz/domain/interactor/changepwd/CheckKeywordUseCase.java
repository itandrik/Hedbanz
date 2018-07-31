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

import android.text.TextUtils;

import com.transcendensoft.hedbanz.data.exception.HedbanzApiException;
import com.transcendensoft.hedbanz.data.repository.UserDataRepositoryImpl;
import com.transcendensoft.hedbanz.domain.CompletableUseCase;
import com.transcendensoft.hedbanz.domain.interactor.changepwd.exception.PasswordResetException;
import com.transcendensoft.hedbanz.domain.repository.UserDataRepository;
import com.transcendensoft.hedbanz.domain.validation.PasswordResetError;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableTransformer;
import io.reactivex.disposables.CompositeDisposable;

/**
 * This class is an implementation of {@link com.transcendensoft.hedbanz.domain.UseCase}
 * that represents a use case for checking server key to reset password
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class CheckKeywordUseCase extends CompletableUseCase<CheckKeywordUseCase.Param> {
    private UserDataRepository mUserDataRepository;
    private PasswordResetException mException;

    @Inject
    public CheckKeywordUseCase(CompletableTransformer completableTransformer,
                                 CompositeDisposable mCompositeDisposable,
                                 UserDataRepositoryImpl userDataRepository) {
        super(completableTransformer, mCompositeDisposable);
        this.mUserDataRepository = userDataRepository;
    }

    @Override
    protected Completable buildUseCaseCompletable(CheckKeywordUseCase.Param param) {
        mException = new PasswordResetException();
        if(isKeywordValid(param.keyword)) {
            return mUserDataRepository.checkKeyword(param.login, param.keyword)
                    .onErrorResumeNext(this::processOnError);
        }
        return Completable.error(mException);
    }

    private boolean isKeywordValid(String keyword){
        if(TextUtils.isEmpty(keyword)){
            mException.addError(PasswordResetError.EMPTY_KEY_WORD);
            return false;
        } else if(keyword.length() != 5) {
            mException.addError(PasswordResetError.INCORRECT_KEY_WORD);
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

    public static class Param{
        private String login;
        private String keyword;

        public Param(String login, String keyword) {
            this.login = login;
            this.keyword = keyword;
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
    }
}
