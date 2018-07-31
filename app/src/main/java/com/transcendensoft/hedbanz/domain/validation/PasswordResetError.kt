package com.transcendensoft.hedbanz.domain.validation

import android.support.annotation.StringRes
import com.transcendensoft.hedbanz.R

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
 * Enum class that describes all errors that can user
 * can receive from server while reseting password
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
enum class PasswordResetError(val id: Int, @StringRes val messageId: Int) {
    EMPTY_LOGIN(1, R.string.login_error_empty_field),
    EMPTY_PASSWORD(2, R.string.login_error_empty_field),
    EMPTY_KEY_WORD(3, R.string.login_error_empty_field),
    EMPTY_LOCALE(4, R.string.login_error_empty_field),
    INCORRECT_LOGIN(5, R.string.register_validate_login),
    INCORRECT_PASSWORD(6, R.string.register_validate_password),
    INCORRECT_KEY_WORD(7, R.string.restore_pwd_error_incorrect_keyword),
    INCORRECT_LOCALE(8, R.string.restore_pwd_error_incorrect_locale),
    KEY_WORD_IS_EXPIRED(9, R.string.restore_pwd_error_keyword_expired),
    NO_SUCH_USER(10, R.string.login_error_no_such_login),
    INCORRECT_PASSWORD_CONFIRMATION(11, R.string.register_validate_confirm_password),
    EMPTY_PASSWORD_CONFIRMATION(12, R.string.register_validate_empty_field),
    UNDEFINED_ERROR(100, R.string.error_undefined_error);

    companion object {
        fun getErrorByCode(code: Int): PasswordResetError {
            for (passwordResetError in PasswordResetError.values()) {
                if (passwordResetError.id == code) {
                    return passwordResetError
                }
            }
            return UNDEFINED_ERROR
        }
    }
}