package com.transcendensoft.hedbanz.presentation.restorepwd

import android.text.TextUtils
import android.widget.EditText
import com.jakewharton.rxbinding2.widget.RxTextView
import com.transcendensoft.hedbanz.domain.entity.User
import com.transcendensoft.hedbanz.domain.interactor.changepwd.ChangePasswordInteractor
import com.transcendensoft.hedbanz.domain.interactor.changepwd.exception.PasswordResetException
import com.transcendensoft.hedbanz.domain.validation.PasswordResetError
import com.transcendensoft.hedbanz.presentation.base.BasePresenter
import com.transcendensoft.hedbanz.utils.RxUtils
import timber.log.Timber
import java.net.ConnectException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Copyright 2018. Andrii Chernysh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
/**
 * Presenter from MVP pattern, that contains
 * methods to process restore password work with repository
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
class RestorePasswordPresenter @Inject constructor(
        private val restorePasswordInteractor: ChangePasswordInteractor
) : BasePresenter<User, RestorePasswordContract.View>(), RestorePasswordContract.Presenter {
    private var isError = false

    override fun updateView() {
        // Stub
    }

    override fun destroy() {
        restorePasswordInteractor.dispose()
    }

    override fun forgotPassword(login: String) {
        view()?.showLoadingDialog()
        isError = false
        restorePasswordInteractor.sendEmailToGetKeyword(login,
                {
                    if (!isError) {
                        view()?.hideLoadingDialog()
                        view()?.goToCheckKeywordFragment()
                    }
                },
                this::processOnError)
    }

    override fun checkKeyword(keyword: String) {
        view()?.showLoadingDialog()
        isError = false
        restorePasswordInteractor.isKeywordCorrect(keyword,
                {
                    if (!isError) {
                        view()?.hideLoadingDialog()
                        view()?.goToResetPasswordFragment()
                    }
                },
                this::processOnError)
    }

    override fun resetPassword(password: String, confirmPassword: String) {
        view()?.showLoadingDialog()
        isError = false
        restorePasswordInteractor.resetPassword(password, confirmPassword,
                {
                    if (!isError) {
                        view()?.hideAll()
                        view()?.finishResetingPassword()
                    }
                },
                this::processOnError)
    }

    private fun processOnError(err: Throwable) {
        view()?.hideLoadingDialog()
        isError = true
        when (err) {
            is PasswordResetException -> {
                for (passwordResetError in err.passwordResetErrors) {
                    processError(passwordResetError)
                }
            }
            is ConnectException -> view()?.showNetworkError()
            else -> {
                Timber.e(err)
                view()?.showServerError()
            }
        }
    }

    private fun processError(passwordResetError: PasswordResetError) {
        val message = passwordResetError.messageId
        when (passwordResetError) {
            PasswordResetError.EMPTY_LOGIN,
            PasswordResetError.INCORRECT_LOGIN,
            PasswordResetError.NO_SUCH_USER -> view()?.showLoginError(message)
            PasswordResetError.EMPTY_PASSWORD,
            PasswordResetError.INCORRECT_PASSWORD -> view()?.showPasswordError(message)
            PasswordResetError.EMPTY_LOCALE,
            PasswordResetError.INCORRECT_LOCALE -> view()?.showLocaleError(message)
            PasswordResetError.EMPTY_KEY_WORD,
            PasswordResetError.INCORRECT_KEY_WORD,
            PasswordResetError.KEY_WORD_IS_EXPIRED -> view()?.showKeywordError(message)
            PasswordResetError.EMPTY_PASSWORD_CONFIRMATION,
            PasswordResetError.INCORRECT_PASSWORD_CONFIRMATION ->
                view()?.showPasswordConfirmationError(message)
            else -> view()?.showServerError()
        }
    }

    /*------------------------------------*
     *------- Animation for smile --------*
     *------------------------------------*/
    override fun initAnimEditTextListener(editText: EditText) {
        addDisposable(
                RxTextView.textChanges(editText)
                        .skip(1)
                        .filter { text -> !TextUtils.isEmpty(text) }
                        .compose(RxUtils.debounceFirst(500, TimeUnit.MILLISECONDS))
                        .doOnNext { view()?.startSmileAnimation() }
                        .mergeWith(RxTextView.textChanges(editText)
                                .skip(1)
                                .debounce(500, TimeUnit.MILLISECONDS)
                                .doOnNext { view()?.stopSmileAnimation() })
                        .subscribe())
    }
}