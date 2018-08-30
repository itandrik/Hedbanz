package com.transcendensoft.hedbanz.presentation.restorepwd

import android.support.annotation.StringRes
import android.widget.EditText
import com.transcendensoft.hedbanz.presentation.base.BaseView

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
 * View and Presenter interfaces contract for
 * restoring password presentation
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
interface RestorePasswordContract {
    interface View : BaseView {
        fun goToForgotPasswordFragment()
        fun goToCheckKeywordFragment()
        fun goToResetPasswordFragment()
        fun showPasswordResendSuccessful()
        fun finishResetingPassword()

        fun showLoginError(@StringRes stringRes: Int)
        fun showPasswordError(@StringRes stringRes: Int)
        fun showPasswordConfirmationError(@StringRes stringRes: Int)
        fun showKeywordError(@StringRes stringRes: Int)
        fun showLocaleError(@StringRes stringRes: Int)

        fun startSmileAnimation()
        fun stopSmileAnimation()
    }

    interface Presenter {
        fun forgotPassword(login: String)
        fun resendKeyword()
        fun checkKeyword(keyword: String)
        fun resetPassword(password: String, confirmPassword: String)
        fun initAnimEditTextListener(editText: EditText)
    }
}