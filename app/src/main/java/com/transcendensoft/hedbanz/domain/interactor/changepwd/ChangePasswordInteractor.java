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

import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;

import javax.inject.Inject;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Interactor that realize GoF pattern facade. It uses 3 use case
 * ForgotPasswordUseCase -> CheckKeywordUseCase -> ResetPasswordUseCase
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class ChangePasswordInteractor {
    @Inject ForgotPasswordUseCase mForgotPasswordUseCase;
    @Inject CheckKeywordUseCase mCheckKeywordUseCase;
    @Inject ResetPasswordUseCase mResetPasswordUseCase;
    @Inject PreferenceManager mPreferenceManager;

    private String mLogin;
    private String mKeyword;

    @Inject
    public ChangePasswordInteractor() {
    }

    public void sendEmailToGetKeyword(String login, Action onComplete,
                                      Consumer<? super Throwable> onError) {
        ForgotPasswordUseCase.Param param = new ForgotPasswordUseCase.Param(login,
                mPreferenceManager.getLocale());

        mLogin = login;
        mForgotPasswordUseCase.execute(param, onComplete, onError);
    }

    public void isKeywordCorrect(String keyword, Action onComplete,
                                 Consumer<? super Throwable> onError) {
        CheckKeywordUseCase.Param param = new CheckKeywordUseCase.Param(mLogin, keyword);

        mKeyword = keyword;
        mCheckKeywordUseCase.execute(param, onComplete, onError);
    }

    public void resetPassword(String password, Action onComplete,
                              Consumer<? super Throwable> onError){
        ResetPasswordUseCase.Param param = new ResetPasswordUseCase.Param(
                mLogin, mKeyword, password);

        mResetPasswordUseCase.execute(param, onComplete, onError);
    }

    public void dispose() {
        mForgotPasswordUseCase.dispose();
        mCheckKeywordUseCase.dispose();
        mResetPasswordUseCase.dispose();
    }
}
