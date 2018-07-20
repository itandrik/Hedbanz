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

import com.transcendensoft.hedbanz.data.repository.UserDataRepositoryImpl;
import com.transcendensoft.hedbanz.domain.CompletableUseCase;
import com.transcendensoft.hedbanz.domain.repository.UserDataRepository;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableTransformer;
import io.reactivex.disposables.CompositeDisposable;

/**
 * This class is an implementation of {@link com.transcendensoft.hedbanz.domain.UseCase}
 * that represents a use case for sending email to
 * user that forgot his password
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class ForgotPasswordUseCase extends CompletableUseCase<ForgotPasswordUseCase.Param> {
    private UserDataRepository mUserDataRepository;

    @Inject
    public ForgotPasswordUseCase(CompletableTransformer completableTransformer,
                                CompositeDisposable mCompositeDisposable,
                                UserDataRepositoryImpl userDataRepository) {
        super(completableTransformer, mCompositeDisposable);
        this.mUserDataRepository = userDataRepository;
    }

    @Override
    protected Completable buildUseCaseCompletable(ForgotPasswordUseCase.Param param) {
        return mUserDataRepository.forgotPassword(param.login, param.locale);
    }

    public static class Param{
        private String login;
        private String locale;

        public Param(String login, String locale) {
            this.login = login;
            this.locale = locale;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getLocale() {
            return locale;
        }

        public void setLocale(String locale) {
            this.locale = locale;
        }
    }
}
