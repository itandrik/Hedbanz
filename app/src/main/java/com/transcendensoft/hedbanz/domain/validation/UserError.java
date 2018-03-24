package com.transcendensoft.hedbanz.domain.validation;
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

import android.support.annotation.StringRes;

import com.transcendensoft.hedbanz.R;

/**
 * All errors that can receive user while log in,
 * sign up or change credentials.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */

public enum UserError {
    NO_SUCH_USER(1, R.string.login_error_no_such_login),
    INCORRECT_PASSWORD(2, R.string.login_error_incorrect_password),
    EMPTY_LOGIN(3, R.string.login_error_empty_field),
    EMPTY_PASSWORD(4, R.string.login_error_empty_field),
    EMPTY_EMAIL(5, R.string.login_error_empty_field),
    SUCH_LOGIN_ALREADY_EXIST(6, R.string.login_error_login_not_available),
    SUCH_EMAIL_ALREADY_USING(7, R.string.login_error_email_not_available),
    INVALID_PASSWORD(8, R.string.register_validate_password),
    INVALID_PASSWORD_CONFIRMATION(8, R.string.register_validate_confirm_password),
    INVALID_LOGIN(9, R.string.register_validate_login),
    INVALID_EMAIL(10, R.string.register_validate_email),
    INVALID_OLD_PASSWORD(11, R.string.credentials_error_confirm_password),
    UNDEFINED_ERROR(100, R.string.error_undefined_error);

    private int errorCode;
    private @StringRes int errorMessage;

    UserError(int errorCode, @StringRes int errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public @StringRes
    int getErrorMessage() {
        return this.errorMessage;
    }

    public static UserError getUserErrorByCode(int code) {
        for (UserError userError : UserError.values()) {
            if (userError.getErrorCode() == code) {
                return userError;
            }
        }
        return UserError.UNDEFINED_ERROR;
    }
}
